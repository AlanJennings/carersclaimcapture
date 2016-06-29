<%@ tag description="Hint Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="hintId" required="false" type="java.lang.String"%>
<%@attribute name="hintText" required="false" type="java.lang.String"%>

<c:if test="${not empty hintText}">
    <c:if test="${empty hintId}">
        <%-- Don't use c:out hintText sometimes contains HTML --%>
        <p class='form-hint'>${hintText}</p>
    </c:if>
    <c:if test="${not empty hintId}">
        <%-- Don't use c:out hintText sometimes contains HTML --%>
        <p class='form-hint' id='${hintId}'>${hintText}</p>
    </c:if>
</c:if>

