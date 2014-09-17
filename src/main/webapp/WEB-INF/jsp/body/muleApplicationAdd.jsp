<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:choose>
    <c:when test="${success}">
        Successfully saved ${applicationName}.
    </c:when>
    <c:otherwise>
        Could not store ${applicationName}.
    </c:otherwise>
</c:choose>