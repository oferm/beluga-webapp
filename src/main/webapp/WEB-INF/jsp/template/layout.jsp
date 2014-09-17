<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<tiles:insertAttribute name="header"/>

<body>
<tiles:insertAttribute name="top"/>
<div id="leftColumn">
    <tiles:insertAttribute name="leftColumn"/>
</div>
<div id="rightColumn">
    <tiles:insertAttribute name="body"/>
</div>
<tiles:insertAttribute name="footer"/>
</body>
</html>
