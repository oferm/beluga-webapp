<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="ui-widget serverSettingsTable">
    <table class="ui-widget ui-widget-content padding-ui-widget-table">
        <thead>
        <tr class="ui-widget-header">
            <th scope="col"/>
            <c:forEach items="${serverNames}" var="serverName">
                <th scope="col">${serverName}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${serverRows}" var="serverRow">
            <tr>
                <td>${serverRow.getApplicationName().toString()}</td>
                <c:forEach items="${serverRow.getServerApplicationValues()}" var="serverApplicationValue">
                    <td>${serverApplicationValue}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot class="tableFooter">
        <tr class="tableFooter">
            <td/>
            <c:forEach items="${serverIds}" var="serverId">
                <td><a id="${serverId}" class="restartServer"
                       href="${pageContext.request.contextPath}/mule/server/restart/${serverId}.html">Restart</a></td>
            </c:forEach>
        </tr>
        </tfoot>
    </table>
</div>

<div id="dialog-restart-confirm" title="Empty the recycle bin?">
    <p>
        <span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
        This will restart the mule server. Are you sure?
    </p>
</div>

<script>
    $('.restartServer').click(function () {
        var url = $(this).attr('href');
        var restartDialog = $('#dialog-restart-confirm');
        restartDialog.data('url', url);
        restartDialog.dialog('open');
//        alert("serverId=" + serverId
//                + "\nurl=" + url);

        //clean up
        url = null;
        restartDialog = null;
        return false;

    });

    $('#dialog-restart-confirm').dialog({
        autoOpen: false,
        resizable: false,
        height: 150,
        modal: true,
        buttons: {
            "Restart": function () {
                var url = $('#dialog-restart-confirm').data('url');
                $.ajax(url)
                        .done(function () {
                            alert("Restarting the Mule server");
                        })
                        .fail(function () {
                            alert("Failed to restart the Mule server");
                        });

                url = null;
                $(this).dialog('close');
            },
            "Cancel": function () {
                $(this).dialog('close');
            }
        }
    });
</script>