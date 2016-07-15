<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" %>
    
<%@ attribute name="id" %>
<%@ attribute name="outerClass" %>
<%@ attribute name="outerStyle" %>
<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:defaultValue value='${pageScope.outerClass}' defaultValue='form-group' var='outerClass' />

<%-- this inherits the errors attribute of the enclosing pageContent tag --%>
<c:if test="${empty pageScope.errors}">
    <c:set var="errors" value="${requestScope.pageContent.errors}" />
</c:if>

<c:if test="${not empty pageScope.errors}">
    <c:set var="hasError" value="${pageScope.errors.hasError(name)}" />
    <c:set var="errorMessage" value="${pageScope.errors.getErrorMessage(name)}" />
</c:if>

<c:if test="${not empty pageScope.outerStyle}">
    <c:set var='outerStyle' value=' style="${pageScope.outerStyle}"' />
</c:if>

<c:if test="${pageScope.hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="<c:out value='${pageScope.outerClass}'/> <c:out value='${pageScope.errorClass}'/>" <c:out value='${pageScope.outerStyle}'/>>
    <c:if test="${pageScope.hasError}" >
        <p class="validation-message">${pageScope.errorMessage}</p>
    </c:if>

    <jsp:doBody />
    
</li>
