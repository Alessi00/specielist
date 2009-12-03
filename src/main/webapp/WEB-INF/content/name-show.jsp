
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <meta name="pageName" content="name" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title> ALA Repository Taxon Name Display</title>
</head>
<body>
      <h1>${title}</h1>
      
      <h2>Type of Object: Taxon Name<br /></h2>
      <table>
        <tr>
            <td>Object Identifier (Guid)</td>
            <td>${guid}</td>
        </tr>
      </table>
      
      <h2>Properties:</h2>

      
      <table class ="propertyTable">
        <!-- Table headings. -->

      <tr>
          <th>Property</th>
          <th>Value</th>
          <th>Harvested</th>
          <th>Source</th>
      </tr>
        <!-- Dynamic table content. -->
        <s:iterator value="objProperties">
          <tr>
            <td><s:property value="relationship" /></td>
            <td>${value}</td>
            <td><s:property value="harvested" /></td>
            <td>
                <a href="/fedora/get/${pid}/<s:property value="sourceDSID"/>"><s:property value="dataSource" /></a>
            </td>
          </tr>
        </s:iterator>

      </table>
      <br />

 <!--   <div id="addedHTML"></div>
    <script>
        var html = "${propertiesHTML}";
        var newText = document.createTextNode(html);
         var ourDiv = document.getElementById('addedHTML');
        ourDiv.appendChild(newText);
    </script>
-->
</body>
</html>
