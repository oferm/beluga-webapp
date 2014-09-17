<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="centerDiv">
    <table>
        <tbody>
        <tr>
            <c:forEach items="${muleServers}" var="muleServer" varStatus="i">
                <td style="border-right: 1px solid #ccc; padding-right: 2px">
                    <a href="${pageContext.request.contextPath}/mule/server/settings/view/${muleServer.getId()}.html">
                            ${muleServer.getName()}
                    </a>
                </td>
            </c:forEach>
            <td id="create-user" class="clickable">Add server</td>
        </tr>
        </tbody>
    </table>
</div>