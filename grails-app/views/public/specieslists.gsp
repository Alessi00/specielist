%{--
  - Copyright (C) 2012 Atlas of Living Australia
  - All Rights Reserved.
  -
  - The contents of this file are subject to the Mozilla Public
  - License Version 1.1 (the "License"); you may not use this file
  - except in compliance with the License. You may obtain a copy of
  - the License at http://www.mozilla.org/MPL/
  -
  - Software distributed under the License is distributed on an "AS
  - IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
  - implied. See the License for the specific language governing
  - rights and limitations under the License.
  --}%
<!doctype html>
<html>
<head>
    <meta name="layout" content="${grailsApplication.config.skin.layout}"/>
    <meta name="breadcrumb" content="Species lists"/>
    <title>Species lists | ${grailsApplication.config.skin.orgNameLong}</title>
</head>

<body class="">
<div id="content" class="row">
    <div class="col-md-12">
        <header id="page-header">
            <div class="row">
                <hgroup class="col-md-8">
                    <h2 class="subject-title">Species lists</h2>
                </hgroup>

                <div class="col-md-4">
                    <span class="pull-right">
                        <a class="btn btn-primary" title="Add Species List"
                           href="${request.contextPath}/speciesList/upload">Upload a list</a>
                        <g:if test="${isLoggedIn}">
                            <a class="btn btn-primary" title="My Lists" href="${request.contextPath}/speciesList/list">My Lists</a>
                        </g:if>
                    </span>
                </div>
            </div><!--.row-->

        </header>
        <div class="inner row" id="public-specieslist">
            <div class="col-md-12">
                <g:if test="${flash.message}">
                    <div class="message alert alert-info">
                        <button type="button" class="close" onclick="$(this).parent().hide()">×</button>
                        <b>Alert:</b> ${flash.message}
                    </div>
                </g:if>
                <p>
                    This tool allows you to upload a list of species, and work with that list within the Atlas.
                    <br/>
                    Click "Upload a list" to upload your own list of taxa.
                </p>
                <g:if test="${lists && total > 0}">
                    <p>
                        Below is a listing of user provided species lists. You can use these lists to work
                        with parts of the Atlas.
                    </p>
                    <g:render template="/speciesList" model="[source:'public']"/>

                </g:if>
                <g:elseif test="${params.q}">
                    <form class="listSearchForm">
                        <p>No Species Lists found for: <b>${params.q}</b></p>
                        <button class="btn btn-primary" type="submit">Clear search</button>
                    </form>
                </g:elseif>
                <g:else>
                    <p>There are no Species Lists available</p>
                </g:else>
            </div>
        </div>
    </div>
</div> <!-- content div -->
</body>
</html>