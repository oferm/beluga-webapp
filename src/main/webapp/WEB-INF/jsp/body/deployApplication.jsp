<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<h1>Deploy application</h1>
<br>

<form:form modelAttribute="uploadItem" method="post" enctype="multipart/form-data" id="deployApplicationForm">
    <c:choose>
        <c:when test="${uploadComplete}">
            The upload of file ${fileName} was completed
            <c:choose>
                <c:when test="${success}">
                    successfully.
                </c:when>
                <c:otherwise>
                    unsuccessfully.
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            Upload and deploy Mule applications (.zip).
            <br>
            Upload locations:
            <div id="dialog-form" title="Upload zip-file for deployment" class="ui-widget"
                 style="margin-top: 20px; margin-bottom: 20px; padding: .6em 10px">
                <table class="ui-widget ui-widget-content padding-ui-widget-table">
                    <thead>
                    <tr class="ui-widget-header">
                        <th scope="col">Server</th>
                        <th scope="col">Deploy URL</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${uploadLocations}" var="uploadLocation" varStatus="i">
                        <tr>
                            <td>${serverNames[i.index]}</td>
                            <td>${uploadLocation}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <fieldset>
                <legend>Select deployable Mule arcive</legend>
                <p>
                    <form:input path="fileData" cssClass="input" type="file"/>
                </p>

                <p>
                    <input id="submitButton" type="submit" class="button" value="Upload and deploy"/>
                </p>
            </fieldset>
        </c:otherwise>
    </c:choose>
</form:form>