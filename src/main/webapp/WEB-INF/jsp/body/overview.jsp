<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<h1>Overview</h1>

<div class="ui-widget serverSettingsTable">
    <table class="ui-widget ui-widget-content padding-ui-widget-table">
        <thead>
        <tr class="ui-widget-header">
            <th scope="col">Application Name</th>
            <c:forEach var="muleServer" items="${muleServerList}">
                <th scope="col">${muleServer.toString()}</th>
                <%--One empty th for each add application icon--%>
                <th scope="col"></th>
                <th scope="col"></th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="muleExistingApp" items="${muleExistingApps}">
            <tr id="${muleExistingApp.getMuleApplication().getName()}">
                <td>
                    <a href="<spring:url value="/mule/application/view/${muleExistingApp.getMuleApplication()}.html"/>">
                            ${muleExistingApp.getMuleApplication()}
                    </a>
                </td>
                <c:forEach var="serverStatus" items="${muleExistingApp.getListOfServerStatus()}"
                           varStatus="serverStatusIndex">
                    <td>${serverStatus.toString()}</td>
                    <c:choose>
                        <c:when test="${serverStatus.toString() == 'Running. Not in database'}">
                            <td class="addToDatabase">
                                <img class="addApplication clickable" alt="Add" title="Add to database"
                                     src="${pageContext.request.contextPath}/images/plus-48x48.png"
                                     height="12" width="12"/>
                            </td>
                            <td></td>
                        </c:when>
                        <c:otherwise>
                            <td></td>
                            <td class="undeploy">
                                <img class="propertiesDelete clickable"
                                     src="${pageContext.request.contextPath}/images/delete16.png"
                                     width="14" height="14" alt="delete" title="Undeploy"/>
                            </td>
                        </c:otherwise>
                    </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:forEach var="muleMissingApp" items="${muleMissingApps}" varStatus="i">
            <tr>
                <td>${muleMissingApp.getMuleApplication()}</td>
                <c:forEach var="serverStatus" items="${muleMissingApp.getListOfServerStatus()}">
                    <td><span style="color: #FF0000; ">${serverStatus.toString()}</span></td>
                </c:forEach>


            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<p>
    Information last updated at ${lastChecked}.
</p>

<div id="dialog-delete-confirm" title="Remove item?">
    <p>
        <span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
        This will remove the application from Beluga and undeploy the application from all Mule servers.<br>
        <br>
        Do you wish to continue?
    </p>
</div>

<script>
    $(".addApplication").click(function () {
        var applicationName = $(this).parent().parent().attr('id');
        var serverId = $(this).parent().attr('id');
        console.log("applicationName", applicationName)
        console.log("serverId", serverId)
        $.post('/mule/application/add.html', {
            name: applicationName,
            muleServerId: serverId
        }).done(function () {
                    alert("Application added successfully.");
                    location.reload(true);
                }
        ).fail(function () {
                    alert("Something went wrong...");
                }
        );
    });


    $("#dialog-delete-confirm").dialog({
        autoOpen: false,
        resizable: false,
        height: 200,
        modal: true,
        buttons: {
            "Delete": function () {
                var thisParentId = $('#dialog-delete-confirm').data('thisParentId');
                $.ajax({
                    url: '/mule/application/properties/delete/' + thisParentId + '.html',
                    type: 'DELETE'
                }).done(function () {
                            var escaped = jqSelector(thisParentId);

                            console.log('#' + escaped);
//                            alert('#' + escaped);
                            $('#' + escaped).fadeOut();
                        });
                delete thisParentId;
                jQuery.removeData($('#dialog-delete-confirm'));
                $(this).dialog("close");
            },
            Cancel: function () {
                $(this).dialog("close");
            }
        }
    });

    $(".propertiesDelete").bind('click', function () {
        var thisParentId = $(this).parent().parent().attr('id');
        $('#dialog-delete-confirm').data("thisParentId", thisParentId);
        $('#dialog-delete-confirm').dialog("open");
    });

    function jqSelector(str) {
//       In the back reference 4 back-slash characters are used to get through the jsp parser stage
        return str.replace(/([ #;?%&,.+*~\':"!^$[\]()=>|\/@])/g, '\\\\$1');
    }

</script>