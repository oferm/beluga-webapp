<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<h1>Mule Server Settings</h1>

<jsp:include page="/WEB-INF/jsp/body/muleServers/listOfMuleServers.jsp"/>

<jsp:include page="/WEB-INF/jsp/body/muleServers/addMuleServer.jsp"/>