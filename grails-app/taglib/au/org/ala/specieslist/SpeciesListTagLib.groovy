package au.org.ala.specieslist

class SpeciesListTagLib {
    static namespace = 'sl'
    def authService

    def getFullNameForUserId = { attrs, body ->
        def displayName = authService.getUserForUserId(attrs.userId)?.displayName
        out << "${displayName?:attrs.userId}"
    }

    /**
     * Generate the URL to the current page minus the fq param specified
     *
     * @attr fqs REQUIRED
     * @attr fq REQUIRED
     */
    def removeFqHref = { attrs, body ->
        def fqList = attrs.fqs
        def fq = attrs.fq
        def remainingFq = fqList - fq
        out << "?fq=" + remainingFq.join("&fq=")
    }
}
