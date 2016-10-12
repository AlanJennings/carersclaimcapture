<%@ tag description="Hint Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="tagNested" %>
<%@ attribute name="hintText" %>
<%@ attribute name="hintTextKey" %>
<%@ attribute name="args" %>
<%@ attribute name="parentName" %>
<%@ attribute name="element" %>

<t:defaultValue value='${pageScope.tagNested}' defaultValue='true' var='tagNested' />

<c:if test="${pageScope.tagNested=='true'}">
    <c:set var='dataTagNestedAttr' value='data-tag-nested="true"' />
</c:if>

<%--TODO need to add support for args here --%>
<c:if test="${empty pageScope.hintText}">
    <c:if test="${not empty pageScope.hintTextKey}">
        <c:set var="hintText"><t:message code="${pageScope.hintTextKey}" args="${pageScope.args}" /></c:set>
    </c:if>
    <c:if test="${empty pageScope.hintTextKey}">
        <c:if test="${empty pageScope.args}">
            <c:set var="args"><t:message parentName="${pageScope.parentName}" element="${pageScope.element}.args" /></c:set>
        </c:if>
        <c:set var="hintText"><t:message parentName="${pageScope.parentName}" element="${pageScope.element}" args="${pageScope.args}" /></c:set>
    </c:if>    
</c:if>

<c:if test="${not empty pageScope.hintText}">
    <%-- Don't use c:out hintText sometimes contains HTML --%>
    <p data-tag-type="hint" ${pageScope.dataTagNestedAttr} class='form-hint'>${hintText}</p>
</c:if>

