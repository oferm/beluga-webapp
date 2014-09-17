<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<table id="propertiesTable" class="tbl-minimalist-horizontal">
    <thead>
    <tr>
        <th scope="col">Name</th>
        <th scope="col">Value</th>
        <th scope="col"/>
        <th scope="col"/>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${listOfProperties}" var="property">
        <tr id="${property.getId()}" class="propertiesRow">
            <td>${property.getName()}</td>
            <td>${property.getValue()}</td>
            <td style="margin: 0; padding: 0;">
                <img class="propertiesEdit" src="${pageContext.request.contextPath}/images/page_white_edit.png"
                     width="14" height="14" alt="edit" title="edit"/>
            </td>
            <td style="margin: 0; padding: 0;">
                <img class="propertiesDelete" src="${pageContext.request.contextPath}/images/delete16.png"
                     width="14" height="14" alt="delete" title="delete"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div id="dialog-form">
    <form id="propertyForm">
        <label for="propertyNameNew">Property name</label>
        <input type="hidden" name="InsertDialog" id="id" value="21"/>
        <input type="text" name="InsertDialog" id="propertyNameNew" value="" disabled="true"/><br>
        <label for="propertyValueNew">Property value</label>
        <input type="text" name="InsertDialog" id="propertyValueNew" value=""/>
    </form>
</div>

<div id="dialog-delete-confirm" title="Remove item?">
    <p>
        <span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
        This item will be permanently deleted. Are you sure?
    </p>
</div>

