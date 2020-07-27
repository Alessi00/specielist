package au.org.ala.specieslist


import com.opencsv.CSVWriter
import grails.converters.JSON

class SpeciesListItemController {

    BieService bieService
    BiocacheService biocacheService
    LoggerService loggerService
    QueryService queryService
    LocalAuthService localAuthService
    int maxLengthForFacet = 30 // Note: is length of _name_ of the facet category/field

    def index() { }

    /**
     * Public display of a species list
     */
    def list(){
        doListDisplay(params)
    }

    /**
     * There is no functional difference between listAuth and list. This method has been retained to support existing
     * links/bookmarks/etc that may refer to it. Both URLs are in the authenticateOnlyIfLoggedInFilterPattern list
     * for CAS authentication.
     */
    def listAuth() {
        doListDisplay(params)
    }

    /**
     * Special (simple) page for displaying "Australia's Species", linked from homepage.
     *
     * @return
     */
    def iconicSpecies() {
        params.id = params.id?:grailsApplication.config.iconicSpecies?.uid?:""
        params.max = params.max?:25

        if (!params.fq || !params.fq.startsWith("kvp")) {
            redirect(action: 'iconicSpecies', params: [fq:'kvp group:Birds'])
        } else {
            try {
                def speciesList = queryService.getSpeciesListByDataResourceUid(params.id)
                if (!speciesList) {
                    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'speciesList.label', default: 'Species List'), params.id])}"
                    render(view: "iconic-list")
                } else {
                    params.max = Math.min(params.max ? params.int('max') : 25, 100)
                    params.sort = params.sort ?: "itemOrder"
                    params.fetch = [kvpValues: 'select']
                    def fqs = params.fq ? [params.fq].flatten().findAll { it != null } : null
                    def baseQueryAndParams = params.fq ? queryService.constructWithFacets(" from SpeciesListItem sli ", fqs, params.id, null) : null
                    //need to get all keys to be included in the table so no need to add the filter.
                    def speciesListItems = params.fq ? SpeciesListItem.executeQuery("select sli " + baseQueryAndParams[0], baseQueryAndParams[1], params) : SpeciesListItem.findAllByDataResourceUid(params.id, params)
                    def totalCount = params.fq ? SpeciesListItem.executeQuery("select count(*) " + baseQueryAndParams[0], baseQueryAndParams[1]).head() : SpeciesListItem.countByDataResourceUid(params.id)
                    def guids = speciesListItems.collect { it.guid }
                    def bieItems = bieService.bulkLookupSpecies(guids)
                    def facets = queryService.generateFacetValues(null, baseQueryAndParams, params.id, null, maxLengthForFacet)
                    log.debug "speciesListItems = ${speciesListItems as JSON}"
                    render(view: 'iconic-list', model: [
                            results: speciesListItems,
                            totalCount: totalCount,
                            bieItems: bieItems,
                            facets: facets
                    ])
                }
            }  catch (Exception e) {
                def msg = "Unable to retrieve species list items. Please let us know if this error persists. <br>Error:<br>" + e.getMessage()
                log.error(msg, e)
                flash.message = msg
                render(view: 'iconic-list')
            }
        }
    }

    private doListDisplay(requestParams) {
        if (requestParams.id) {
            try {
                //check to see if the list exists
                def speciesList = queryService.getSpeciesListByDataResourceUid(requestParams.id)
                if (!speciesList) {
                    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'speciesList.label', default: 'Species List'), requestParams.id])}"
                    redirect(controller: "public", action: "speciesLists")
                } else {
                    if (requestParams.message)
                        flash.message = requestParams.message
                    requestParams.max = Math.min(requestParams.max ? requestParams.int('max') : 10, 100)
                    requestParams.sort = requestParams.sort ?: "itemOrder"
                    requestParams.offset = requestParams.int('offset') ?: 0
                    requestParams.fetch = [kvpValues: 'select']
                    log.debug(requestParams.toQueryString())

                    def fqs = requestParams.fq ? [requestParams.fq].flatten().findAll { it != null } : null

                    def baseQueryAndParams = requestParams.fq ? queryService.constructWithFacets(" from SpeciesListItem sli ", fqs, requestParams.id, requestParams.q) : null
                    log.debug(baseQueryAndParams?.toString())

                    // to sort on a column 'order by' clause has to be added explicitly since executeQuery function does
                    // not accept sort and order as named parameters. Named parameters accepted by executeQuery includes
                    // max and offset. check documentation for more details.
                    List baseQueryAndParamsForListingSLI = baseQueryAndParams?.clone()
                    if (requestParams.sort && baseQueryAndParams) {
                        baseQueryAndParamsForListingSLI[0] += " order by sli.${requestParams.sort} ${requestParams.order ?: 'asc'}"
                    }
                    def noMatchCount = queryService.getNoMatchCountByParams(requestParams, baseQueryAndParams)

                    log.debug("Checking speciesList: " + speciesList)
                    log.debug("Checking editors: " + speciesList.editors)
                    render(view: 'list', model: [
                            speciesList: speciesList,
                            params: requestParams,
                            results: queryService.getSpeciesListItemsByParams(requestParams, baseQueryAndParamsForListingSLI),
                            totalCount: queryService.getTotalCountByParams(requestParams, baseQueryAndParams),
                            noMatchCount: noMatchCount,
                            distinctCount: queryService.getDistinctCountByParams(requestParams, baseQueryAndParams),
                            hasUnrecognised: noMatchCount > 0,
                            keys: queryService.getSpeciesListKVPKeysByDataResourceUid(requestParams.id),
                            downloadReasons: loggerService.getReasons(),
                            users: queryService.getUsersForList(),
                            facets: queryService.generateFacetValues(fqs, baseQueryAndParams, requestParams.id, requestParams.q, maxLengthForFacet),
                            fqs : fqs
                    ])
                }
            } catch (Exception e) {
                log.error("Unable to view species list items.", e)
                render(view: '../error', model: [message: "Unable to retrieve species list items. Please let us know if this error persists. <br>Error:<br>" + e.getMessage()])
            }
        } else {
            //redirect to the public species list page
            redirect(controller: "public", action: "speciesLists")
        }
    }

    //Todo: not used ?
    def facetsvalues(){
        if(params.id){
          def result = SpeciesListItem.executeQuery('select kvp.key, kvp.value, kvp.vocabValue, count(sli) as cnt ' +
                  'from SpeciesListItem as sli join sli.kvpValues  as kvp where sli.dataResourceUid=? ' +
                  'group by kvp.key, kvp.value, kvp.vocabValue order by kvp.key,cnt desc', params.id)
          //group the same properties keys together
          def properties = result.groupBy { it[0] }
          def map = [:]
          map.listProperties = properties
          render map as JSON
        }
        null
    }

    /**
     * Downloads the records for the supplied species list
     * @return
     */
    def downloadList(){
        if (params.id){
            params.fetch = [ kvpValues: 'join' ]
            log.debug("Downloading Species List")
            def keys = SpeciesListKVP.executeQuery("select distinct key from SpeciesListKVP where dataResourceUid='"+params.id+"'")
            def fqs = params.fq?[params.fq].flatten().findAll{ it != null } : null
            def baseQueryAndParams = queryService.constructWithFacets(" from SpeciesListItem sli ", fqs, params.id, params.q)
            def sli = SpeciesListItem.executeQuery("Select sli " + baseQueryAndParams[0], baseQueryAndParams[1])
            //def sli =SpeciesListItem.findAllByDataResourceUid(params.id,params)
            def out = new StringWriter()
            def csvWriter = new CSVWriter(out)
            def header =  ["Supplied Name","guid","scientificName","family","kingdom"]
            header.addAll(keys)
            log.debug(header?.toString())
            csvWriter.writeNext(header as String[])
            sli.each {
                def values = keys.collect{key->it.kvpValues.find {kvp -> kvp.key == key}}.collect { kvp -> kvp?.vocabValue?:kvp?.value}
                def row = [it.rawScientificName, it.guid, it.matchedName, it.family, it.kingdom]
                row.addAll(values)
                csvWriter.writeNext(row as String[])
            }
            csvWriter.close()
            def filename = params.file?:"list.csv"
            if(!filename.toLowerCase().endsWith('.csv')){
                filename += '.csv'
            }

            response.addHeader("Content-Disposition", "attachment;filename="+filename);
            render(contentType: 'text/csv', text:out.toString())
        }
    }

    /**
     * Returns BIE details about the supplied guid
     * @return
     */
    def itemDetails(){
        log.debug("Returning item details for " + params)
        if(params.guids){
            render bieService.bulkLookupSpecies(params.guid) as JSON
        } else {
            null
        }
    }
}