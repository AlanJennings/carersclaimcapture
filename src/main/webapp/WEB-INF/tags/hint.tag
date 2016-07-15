<%@ tag description="Hint Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="hintText" required="false" type="java.lang.String"%>
<%@ attribute name="hintTextKey" required="false" type="java.lang.String"%>
<%@ attribute name="parentName" required="false" type="java.lang.String"%>
<%@ attribute name="element" required="false" type="java.lang.String"%>

<%--TODO need to add support for args here --%>
<c:if test="${empty pageScope.hintText}">
    <c:if test="${not empty pageScope.hintTextKey}">
        <c:set var="hintText"><t:message code="${pageScope.hintTextKey}" /></c:set>
    </c:if>
    <c:if test="${empty pageScope.hintTextKey}">
        <c:set var="hintText"><t:message parentName="${pageScope.parentName}" element="${pageScope.element}" /></c:set>
    </c:if>    
</c:if>

<c:if test="${not empty pageScope.hintText}">
    <%-- Don't use c:out hintText sometimes contains HTML --%>
    <p class='form-hint'>${hintText}</p>
</c:if>

