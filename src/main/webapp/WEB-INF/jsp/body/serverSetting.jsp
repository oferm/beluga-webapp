<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<h1>Mule Server Settings</h1>

<h2>Server name: ${serverName}</h2>

<jsp:include page="/WEB-INF/jsp/body/muleServers/listOfMuleServers.jsp"/>
<jsp:include page="/WEB-INF/jsp/body/muleServers/addMuleServer.jsp"/>

<div class="ui-widget serverSettingsTable">
    <table class="ui-widget ui-widget-content padding-ui-widget-table">
        <thead>
        <tr class="ui-widget-header">
            <th scope="col">Property name</th>
            <th scope="col">Property value</th>
            <th scope="col"/>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${muleServerProperties}" var="muleServerProperty">
            <c:if test="${muleServerProperty.key != 'id'}">
                <tr id="${muleServerProperty.key}">
                    <td class="property-key">${muleServerProperty.key}</td>
                    <td class="property-value">${muleServerProperty.value}</td>
                    <td style="margin: 0; padding: 0;">
                        <img class="propertiesEdit" src="${pageContext.request.contextPath}/images/page_white_edit.png"
                             width="14" height="14" alt="edit" title="edit"/>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</div>

<div id="dialog-form-edit">
    <form id="propertyForm">
        <label for="propertyNameNew">Property name</label>
        <input type="hidden" name="InsertDialog" id="id" value="21"/>
        <input type="text" name="InsertDialog" id="propertyNameNew" value="" disabled="true"/><br>
        <label for="propertyValueNew">Property value</label>
        <input type="text" name="InsertDialog" id="propertyValueNew" value=""/>
    </form>
</div>

<script>
    $(".propertiesEdit").bind('click', function () {
        var thisParentId = $(this).parent().parent().attr('id');
        var PROPERTY_VALUE_OLD = "tr#" + thisParentId + " td:nth-child(2)";
        var PROPERTY_NAME_OLD = "tr#" + thisParentId + " td:nth-child(1)";
        updateFormProperties();
        var $dialog = $('#dialog-form-edit');
        $dialog.data("PROPERTY_VALUE_OLD", PROPERTY_VALUE_OLD);
        $dialog.data("PROPERTY_NAME_OLD", PROPERTY_NAME_OLD);
        $dialog.data("thisParentId", thisParentId);
        $dialog.dialog("open");

        function updateFormProperties() {
            $('#propertyNameNew').val($(PROPERTY_NAME_OLD).html());
            $('#propertyValueNew').val($(PROPERTY_VALUE_OLD).html());
            $('#id').val(thisParentId);
        }

        return false;
    });

    function shutdownEditProperties() {
        $('#propertyForm').find(':input').each(function () {
            switch (this.type) {
                case 'password':
                case 'select-multiple':
                case 'select-one':
                case 'text':
                case 'textarea':
                    $(this).val('');
                    break;
                case 'checkbox':
                case 'radio':
                    this.checked = false;
            }
        });
        $(this).dialog("close");
    }

    function updateEditProperties(propertyName, propertyValue) {
        console.log("Entering updateEditProperties with propertyName=" + propertyName);
        console.log("Entering updateEditProperties with propertyValue=" + propertyValue);
        function updateEditProperty(propertyName, propertyPlace) {
            console.log("Entering updateEditProperties inner with propertyName=" + propertyName);
            console.log("Entering updateEditProperties inner with propertyPlace=" + propertyPlace);
            $(propertyName).fadeOut()
                    .queue(function (n) {
                        $(propertyName).html(propertyPlace);
                        n();
                    }).fadeIn();
        }

        updateEditProperty(propertyName, $('#propertyNameNew').val());
        updateEditProperty(propertyValue, $('#propertyValueNew').val());
    }

    $('#dialog-form-edit').dialog({
        autoOpen: false,
        height: 200,
        width: 350,
        title: "Edit Property",
        modal: true,

        buttons: [
            {
                text: "Submit",
                click: function () {
                    var dialog = $('#dialog-form-edit');
                    var propValueOld = dialog.data('PROPERTY_VALUE_OLD');
                    var propNameOld = dialog.data('PROPERTY_NAME_OLD');
                    var thisParentId = dialog.data('thisParentId');

                    $.post('/mule/server/settings/edit.html', {
                        id: <c:out value="${muleServerId}"/>,
                        name: $('#propertyNameNew').val(),
                        value: $('#propertyValueNew').val()
                    });

                    updateEditProperties(propNameOld, propValueOld);
                    jQuery.removeData(dialog);

                    shutdownEditProperties.call($(this));
                }
            },
            {
                text: "Cancel",
                click: function () {
                    shutdownEditProperties.call($(this));
                }
            }
        ]
    });
</script>