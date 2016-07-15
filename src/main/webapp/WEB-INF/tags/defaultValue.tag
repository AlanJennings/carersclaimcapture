<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="value" required="true" type="java.lang.Object" %>
<%@ attribute name="defaultValue" required="true" type="java.lang.Object" %>

<%-- rtexprvalue is supposed to be optional and default to false, but it doesn't --%>
<%@ attribute name="var" required="true" rtexprvalue="false" %>

<%@ variable name-from-attribute="var" alias="result" scope="AT_END" %>

<c:if test="${empty pageScope.value}" >
    <c:set var="result" value="${pageScope.defaultValue}" />
</c:if>
<c:if test="${not empty pageScope.value}" >
    <c:set var="result" value="${pageScope.value}" />
</c:if>
