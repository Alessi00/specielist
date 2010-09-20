<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta name="pageName" content="species"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-ui-1.8.custom.min.js"></script>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bie-theme/jquery-ui-1.8.custom.css" charset="utf-8">-->
    <script type="text/javascript" src="http://bie.ala.org.au/static/js/jquery-ui-1.8.custom.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.highlight-3.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            var facetLinksSize = $("ul#subnavlist li").size();
            if (facetLinksSize == 0) {
                // Hide an empty facet link list
                $("#facetBar > h4").hide();
                $("#facetBar #navlist").hide();
            }

            /* Accordion widget */
            var icons = {
                header: "ui-icon-circle-arrow-e",
                headerSelected: "ui-icon-circle-arrow-s"
            };
            $("#accordion0").accordion({
                icons: icons,
                collapsible: true,
                autoHeight: false
            });
            // more/fewer search option links
            $("#refineMore a").click(function(e) {
                e.preventDefault();
                $("#accordion").show();
                $("#refineLess").show();
                $("#refineMore").hide();
                $.cookie("bie-refine", "show"); // set cookie
            });
            $("#refineLess a").click(function(e) {
                e.preventDefault();
                $("#accordion").hide();
                $("#refineLess").hide();
                $("#refineMore").show();
                $.cookie("bie-refine", "hide"); // set cookie
            });
            // use cookie to remeber state of the facet links
            var refineState = $.cookie("bie-refine");
            if (refineState == "show") {
                $("#refineMore a").click();
            }
            // listeners for sort widgets
            $("select#sort").change(function() {
                var val = $("option:selected", this).val();
                reloadWithParam('sort',val);
            });
            $("select#dir").change(function() {
                var val = $("option:selected", this).val();
                reloadWithParam('dir',val);
            });
            $("select#per-page").change(function() {
                var val = $("option:selected", this).val();
                reloadWithParam('pageSize',val);
            });
            // highlight search terms in results
            //$('.results p').highlight('${queryJsEscaped}');
            var words = '${queryJsEscaped}';
            $.each(words.split(" "), function(idx, val) { $('.results p').highlight(val); });
        });

        // jQuery getQueryParam Plugin 1.0.0 (20100429)
        // By John Terenzio | http://plugins.jquery.com/project/getqueryparam | MIT License
        // Adapted by Nick dos Remedios to handle multiple params with same name - return a list
        (function ($) {
            // jQuery method, this will work like PHP's $_GET[]
            $.getQueryParam = function (param) {
                // get the pairs of params fist
                var pairs = location.search.substring(1).split('&');
                var values = [];
                // now iterate each pair
                for (var i = 0; i < pairs.length; i++) {
                    var params = pairs[i].split('=');
                    if (params[0] == param) {
                        // if the param doesn't have a value, like ?photos&videos, then return an empty srting
                        //return params[1] || '';
                        values.push(params[1]);
                    }
                }

                if (values.length > 0) {
                    return values;
                } else {
                    //otherwise return undefined to signify that the param does not exist
                    return undefined;
                }

            };
        })(jQuery);

        function removeFacet(facet) {
            var q = $.getQueryParam('q'); //$.query.get('q')[0];
            var fqList = $.getQueryParam('fq'); //$.query.get('fq');
            var paramList = [];
            if (q != null) {
                paramList.push("q=" + q);
            }
            //alert("this.facet = "+facet+"; fqList = "+fqList.join('|'));

            if (fqList instanceof Array) {
                //alert("fqList is an array");
                for (var i in fqList) {
                    //alert("i == "+i+"| fq = "+fqList[i]);
                    if (decodeURI(fqList[i]) == facet) {
                        //alert("removing fq: "+fqList[i]);
                        fqList.splice(fqList.indexOf(fqList[i]),1);
                    }
                }
            } else {
                //alert("fqList is NOT an array");
                if (decodeURI(fqList) == facet) {
                    fqList = null;
                }
            }
            //alert("(post) fqList = "+fqList.join('|'));
            if (fqList != null) {
                paramList.push("fq=" + fqList.join("&fq="));
            }

            window.location.replace(window.location.pathname + '?' + paramList.join('&'));
        }

        /**
         * Catch sort drop-down and build GET URL manually
         */
        function reloadWithParam(paramName, paramValue) {
            var paramList = [];
            var q = $.getQueryParam('q'); //$.query.get('q')[0];
            var fqList = $.getQueryParam('fq'); //$.query.get('fq');
            var sort = $.getQueryParam('sort');
            var dir = $.getQueryParam('dir');
            // add query param
            if (q != null) {
                paramList.push("q=" + q);
            }
            // add filter query param
            if (fqList != null) {
                paramList.push("fq=" + fqList.join("&fq="));
            }
            // add sort param if already set
            if (paramName != 'sort' && sort != null) {
                paramList.push('sort' + "=" + sort);
            }

            if (paramName != null && paramValue != null) {
                paramList.push(paramName + "=" +paramValue);
            }

            //alert("params = "+paramList.join("&"));
            //alert("url = "+window.location.pathname);
            window.location.replace(window.location.pathname + '?' + paramList.join('&'));
        }

    </script>
    <style type="text/css" media="screen">
        .highlight { font-weight: bold; }
        div.results span.highlight { display: inline; }
        span.FieldName { padding-left: 5px; }
        #facets > h3 {
            border-bottom: 1px solid #E8EACE;
            border-top: 1px solid #E8EACE;
            font-size: 1em;
            font-weight: bold;
            line-height: 2em;
        }
        #subnavlist li {
            text-transform: capitalize;
        }
    </style>
    <title>Species Search - ${query}</title>
    <link rel="stylesheet" href="http://test.ala.org.au/wp-content/themes/ala/css/bie.css" type="text/css" media="screen" charset="utf-8"/>
<!--    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/screen.css" type="text/css" media="screen" charset="utf-8"/>-->
</head>
<body>
<c:if test="${empty searchResults.results}">
    <div  id="searchResults"><p>Search for <span style="font-weight: bold"><c:out value="${query}"/></span> did not match any documents</p></div>
</c:if>
<c:if test="${not empty searchResults.results}">
    <c:set var="pageTitle">
        <c:if test="${not empty title}">${title}</c:if>
        <c:if test="${empty title}">Search Results</c:if>
    </c:set>
    <div id="header">
        <div id="breadcrumb">
            <a href="http://test.ala.org.au">Home</a>
            <a href="http://test.ala.org.au/explore/">Explore</a>
            <span class="current">Search the Atlas</span>
        </div>
        <div class="astrisk" style="display:none">
            <h1>Search Results</h1>
            <p>Looking for specimen or occurrence data? <a href="http://biocache.ala.org.au/occurrences/search?q=${param['q']}">Search for occurrence records</a></p>
        </div>
    </div><!--close header-->
    <div id="refine-results" class="section no-margin-top">
        <h2>Refine results</h2>
        <h3><strong><fmt:formatNumber value="${searchResults.totalRecords}" pattern="#,###,###"/></strong> results
            returned for <strong><a href="?q=${queryJsEscaped}">${queryJsEscaped}</a></strong></h3>
    </div>
    <div id="searchResults">
        <div id="facets">
            <c:if test="${not empty TAXON or not empty REGION or not empty INSTITUTION or not empty COLLECTION or not empty DATAPROVIDER or not empty DATASET}">
                <c:set var="taxon" scope="session"><c:out value="${TAXON}"/></c:set>
                <c:set var="region" scope="session"><c:out value="${REGION}"/></c:set>
                <c:set var="institution" scope="session"><c:out value="${INSTITUTION}"/></c:set>
                <c:set var="collection" scope="session"><c:out value="${COLLECTION}"/></c:set>
                <c:set var="dataprovider" scope="session"><c:out value="${DATAPROVIDER}"/></c:set>
                <c:set var="dataset" scope="session"><c:out value="${DATASET}"/></c:set>
            </c:if>
            <h3><span class="FieldName">Section</span></h3>
            <div id="subnavlist">
                <ul>
<!--                    <li><a href="#">Site pages</a> <span>(43)</span></li>-->
                    <li class="active">Species</li>
                    <c:if test="${not empty region}">
                        <li><a href="${pageContext.request.contextPath}/regions/search?q=${param['q']}">Regions</a></li>
                    </c:if>
<!--                        <li><a href="/biocache-webapp/occurrences/search?q=${param['q']}"><strike>Occurrence Records</strike></a></li>-->
                    <c:if test="${not empty institution}">
                        <li><a href="${pageContext.request.contextPath}/institutions/search?q=${param['q']}">Institutions</a></li>
                    </c:if>
                    <c:if test="${not empty collection}">
                        <li><a href="${pageContext.request.contextPath}/collections/search?q=${param['q']}">Collections</a></li>
                    </c:if>
                    <c:if test="${not empty dataprovider}">
                        <li><a href="${pageContext.request.contextPath}/dataproviders/search?q=${param['q']}">Data Providers</a></li>
                    </c:if>
                    <c:if test="${not empty dataset}">
                        <li><a href="${pageContext.request.contextPath}/datasets/search?q=${param['q']}">Data Sets</a></li>
                    </c:if>
                </ul>
            </div>
            <div id="refineMore"><a href="#">More Search Options</a></div>
            <div id="accordion">
                <c:if test="${not empty query}">
                    <c:set var="queryParam">q=<c:out value="${query}" escapeXml="true"/><c:if test="${not empty param.fq}">&fq=${fn:join(paramValues.fq, "&fq=")}</c:if></c:set>
                </c:if>
                <c:forEach var="facetResult" items="${searchResults.facetResults}">
                    <c:if test="${!fn:containsIgnoreCase(facetQuery, facetResult.fieldResult[0].label) && !fn:containsIgnoreCase(facetResult.fieldName, 'idxtype')}">
                        <h3><span class="FieldName"><fmt:message key="facet.${facetResult.fieldName}"/></span></h3>
                        <div id="subnavlist">
                            <ul>
                                <c:set var="lastElement" value="${facetResult.fieldResult[fn:length(facetResult.fieldResult)-1]}"/>
                                <c:if test="${lastElement.label eq 'before'}">
                                    <li><c:set var="firstYear" value="${fn:substring(facetResult.fieldResult[0].label, 0, 4)}"/>
                                        <a href="?${queryParam}&fq=${facetResult.fieldName}:[* TO ${facetResult.fieldResult[0].label}]">Before ${firstYear}</a>
                                        (<fmt:formatNumber value="${lastElement.count}" pattern="#,###,###"/>)
                                    </li>
                                </c:if>
                                <c:forEach var="fieldResult" items="${facetResult.fieldResult}" varStatus="vs">
                                    <c:set var="dateRangeTo"><c:choose><c:when test="${vs.last}">*</c:when><c:otherwise>${facetResult.fieldResult[vs.count].label}</c:otherwise></c:choose></c:set>
                                    <c:choose>
                                        <c:when test="${fn:containsIgnoreCase(facetResult.fieldName, 'occurrence_date') && fn:endsWith(fieldResult.label, 'Z')}">
                                            <li><c:set var="startYear" value="${fn:substring(fieldResult.label, 0, 4)}"/>
                                                <a href="?${queryParam}&fq=${facetResult.fieldName}:[${fieldResult.label} TO ${dateRangeTo}]">${startYear} - ${startYear + 10}</a>
                                                (<fmt:formatNumber value="${fieldResult.count}" pattern="#,###,###"/>)</li>
                                        </c:when>
                                        <c:when test="${fn:endsWith(fieldResult.label, 'before')}"><%-- skip --%></c:when>
                                        <c:when test="${not empty facetMap[facetResult.fieldName] && fn:containsIgnoreCase(fieldResult.label, facetMap[facetResult.fieldName])}">
                                            <li><a href="#" onClick="removeFacet('${facetResult.fieldName}:${fieldResult.label}'); return false;" class="facetCancelLink">&lt; Any <fmt:message key="facet.${facetResult.fieldName}"/></a><br/>
                                            <b><fmt:message key="${fieldResult.label}"/></b></li>
                                            <%-- foo[${facetResult.fieldName}]: ${facetMap[facetResult.fieldName]} | ${fieldResult.label} | bar:${facetMap['kingdom']} --%>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="?${queryParam}&fq=${facetResult.fieldName}:${fieldResult.label}"><fmt:message key="${fieldResult.label}"/></a>
                                            (<fmt:formatNumber value="${fieldResult.count}" pattern="#,###,###"/>)
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
            <div id="refineLess"><a href="#">Fewer Search Options</a></div>
        </div><!--facets-->
        <div class="solrResults">
            <div id="dropdowns">
                <div id="resultsStats">
                    <label for="per-page">Results per page</label>
                    <select id="per-page" name="per-page">
                        <option value="10" <c:if test="${param.pageSize eq '10'}">selected</c:if>>10</option>
                        <option value="20" <c:if test="${param.pageSize eq '20'}">selected</c:if>>20</option>
                        <option value="50" <c:if test="${param.pageSize eq '50'}">selected</c:if>>50</option>
                        <option value="100" <c:if test="${param.pageSize eq '100'}">selected</c:if>>100</option>
                    </select>
                </div>
                <div id="sortWidget">
                    <select id="sort" name="sort">
                        <option value="score" <c:if test="${param.sort eq 'score'}">selected</c:if>>best match</option>
                        <option value="scientificNameRaw" <c:if test="${param.sort eq 'scientificNameRaw'}">selected</c:if>>scientific name</option>
                        <!--                            <option value="rank">rank</option>-->
                        <option value="commonNameSort" <c:if test="${param.sort eq 'commonNameSort'}">selected</c:if>>common name</option>
                        <option value="rank" <c:if test="${param.sort eq 'rank'}">selected</c:if>>taxon rank</option>
                    </select>
                    sort order
                    <select id="dir" name="dir">
                        <option value="asc" <c:if test="${param.dir eq 'asc'}">selected</c:if>>normal</option>
                        <option value="desc" <c:if test="${param.dir eq 'desc'}">selected</c:if>>reverse</option>
                    </select>

                    <input type="hidden" value="${pageTitle}" name="title"/>

                </div><!--sortWidget-->
            </div><!--drop downs-->
            <div class="results">
                <c:forEach var="taxonConcept" items="${searchResults.results}">
                    <h4><a href="${pageContext.request.contextPath}/species/${taxonConcept.guid}" class="occurrenceLink"><alatag:formatSciName rankId="${taxonConcept.rankId}" name="${taxonConcept.name}" acceptedName="${taxonConcept.acceptedConceptName}"/></a></h4>
                    <c:set var="imageUrl">
                        <c:choose>
                            <c:when test="${not empty taxonConcept.thumbnail}">${taxonConcept.thumbnail}</c:when>
                            <c:otherwise>${pageContext.request.contextPath}/static/images/noImage100.jpg</c:otherwise>
                        </c:choose>
                    </c:set>
                    <p><a href="${pageContext.request.contextPath}/species/${taxonConcept.guid}" class="occurrenceLink"><img class="alignright" src="${imageUrl}" width="91" height="91" alt="species image thumbnail"/></a>
                        ${taxonConcept.commonName} 
                        <span><strong>Rank</strong>: ${taxonConcept.rank}</span>
                        <c:if test="${not empty taxonConcept.highlight}"><span><b>...</b> ${taxonConcept.highlight} <b>...</b></span></c:if>
                    </p>
                    
                </c:forEach>
            </div><!--close results-->
            <div id="searchNavBar">
                <alatag:searchNavigationLinks totalRecords="${searchResults.totalRecords}" startIndex="${searchResults.startIndex}"
                     lastPage="${lastPage}" pageSize="${searchResults.pageSize}"/>
            </div>
            <div id="searchNavBar" style="display: none">
                <ul>
                    <!-- coreParams = ?q=kangaroo&sort=&dir=&pageSize=10 || lastPage = 10 || startIndex = 0 || pageNumber = 1 -->
                    <li id="prevPage">&laquo; Previous</li>
                    <li class="currentPage">1</li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=10&title=Search Results">2</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=20&title=Search Results">3</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=30&title=Search Results">4</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=40&title=Search Results">5</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=50&title=Search Results">6</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=60&title=Search Results">7</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=70&title=Search Results">8</a></li>
                    <li><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=80&title=Search Results">9</a></li>
                    <li id="nextPage"><a href="?q=kangaroo&sort=&dir=&pageSize=10&start=10&title=Search Results">Next &raquo;</a></li>
                </ul>
            </div>
        </div><!--solrResults-->
    </div>
</c:if>
</body>
</html>
