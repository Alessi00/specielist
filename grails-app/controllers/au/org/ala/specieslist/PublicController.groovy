package au.org.ala.specieslist

class PublicController {

    def authService
    def queryService
    def localAuthService

    def index() {
        //redirect to the correct type of list based on whether or not the use is logged in
        try {
            def userId = authService.userId
            log.debug("userId: " + userId)
            redirect(action: 'speciesLists')
        } catch (Exception e){
            render(view: '../error', model: [message: "Unable to retrieve species lists. Please let us know if this error persists. <br>Error:<br>" + e.getMessage()])
        }
    }

    def speciesLists(){

        params.max = Math.min(params.max ? params.int('max') : 25, 1000)
        params.sort = params.sort ?: "listName"
        if(params.isSDS){
            //to ensure backwards compatibility for a commonly used URL
            params.isSDS = "eq:true"
        }

        try {
            def lists = queryService.getFilterListResult(params, true)
            def facets = queryService.getFacetCounts(params)
            log.info "lists = ${lists.size()} || count = ${lists.totalCount}"
            render (view:'specieslists', model:[
                    isAdmin:localAuthService.isAdmin(),
                    lists:lists,
                    total:lists.totalCount,
                    facets:facets,
                    selectedFacets:queryService.getSelectedFacets(params)
            ])
        } catch(Exception e) {
            log.error "Error requesting species Lists: " ,e
            response.status = 404
            render(view: '../error', model: [message: "Unable to retrieve species lists. Please let us know if this error persists. <br>Error:<br>" + e.getMessage()])
        }
    }

    def sdsLists() {
        redirect(action:'speciesLists', params:["isSDS":"eq:true"])
    }
}
