<%--
    Document   : main.jsp (sitemesh decorator file)
    Created on : 18/09/2009, 13:57
    Author     : dos009
--%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
    <head>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js" type="text/javascript"></script>
        <title><decorator:title default="ALA Biodiversity Harvester" /></title>
        <s:head/>
        <decorator:head />
        <link rel="stylesheet" href="<s:url value="/css/screen.css"/>" type="text/css" media="screen" charset="utf-8">
        <link rel="stylesheet" href="<s:url value="/css/print.css"/>" type="text/css" media="print" charset="utf-8">
        <!--[if IE]><link rel="stylesheet" href="<s:url value="/css/lib/ie.css"/>" type="text/css" media="screen, projection" /><![endif]-->
    </head>
    <body>
        <div id="page">
            <div id="header">
                <div id="menuSearch">
                    <s:form action="/search/searchSOLR" namespace="search" method="GET" theme="simple">
                        <s:textfield key="propertyValue" size="25"/>
                        <s:submit value="Search"/>
                    </s:form>
                </div>
                <div id="headerLogo">
                    <a href="${pageContext.request.contextPath}/"><img src="<s:url value="/images/ALA-logo-50px.gif"/>" alt="ALA Logo" id="headerLogo" border="0"/></a>
                </div>
                <h1 id="headerTitle">Biodiversity Information Explorer</h1>
            </div>
            <div id="topmenu">
                <s:set name="pageName"><decorator:getProperty property="meta.pageName"/></s:set>
                <ul class="tabs">
                    <li class="<s:if test="%{#pageName == 'home'}">active</s:if> first"><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li class="<s:if test="%{#pageName == 'datastream'}">active</s:if>"><a href="${pageContext.request.contextPath}/datastream">Show&nbsp;All</a></li>
                    <li class="<s:if test="%{#pageName == 'taxon'}">active</s:if>"><a href="${pageContext.request.contextPath}/taxon">Taxon&nbsp;Concepts</a></li>
                    <li class="<s:if test="%{#pageName == 'name'}">active</s:if>"><a href="${pageContext.request.contextPath}/name">Taxon&nbsp;Names</a></li>
                    <li class="<s:if test="%{#pageName == 'pub'}">active</s:if>"><a href="${pageContext.request.contextPath}/pub">Publications</a></li>
                    <li class="<s:if test="%{#pageName == 'image'}">active</s:if>"><a href="${pageContext.request.contextPath}/image">Images</a></li>
                    <li class="<s:if test="%{#pageName == 'html'}">active</s:if> last"><a href="${pageContext.request.contextPath}/html">HTML&nbsp;Pages</a></li>
                </ul>                
            </div>
            <div id="body">
                <decorator:body />
            </div>
            <div id="footer" class="span-24 prepend-top append-bottom last">
                <div id="copy">&nbsp;&copy;2009 <a href="mailto:info@ala.org.au">Atlas of Living Australia</a></div>
                <ul class="tabs">
                    <li><a href="${pageContext.request.contextPath}/search/RI">RI Search</a></li>
                    <li><a href="${pageContext.request.contextPath}/harvest/setup">Harvest</a></li>
                    <li><a href="${pageContext.request.contextPath}/glossary">Glossary</a></li>
                </ul>
            </div>
        </div>
    </body>
</html>