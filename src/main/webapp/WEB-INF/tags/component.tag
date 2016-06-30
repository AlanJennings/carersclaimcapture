<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="name" required="true" type="java.lang.String"%>

<%@attribute name="id" required="false" type="java.lang.String"%>
<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="outerStyle" required="false" type="java.lang.String"%>
<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<%-- Start of the default values --%>
<c:if test="${not empty errors}">
    <c:set var="hasError" value="${errors.hasError(name)}" />
    <c:set var="errorMessage" value="${errors.getErrorMessage(name)}" />
</c:if>

<c:if test="${empty outerClass}" >
    <c:set var="outerClass" value="form-group" />
</c:if>
<%-- End of the default values --%>

<c:if test="${not empty outerStyle}">
    <c:set var='outerStyle' value=' style="${outerStyle}"' />
</c:if>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="<c:out value='${outerClass}'/> <c:out value='${errorClass}'/> <c:out value='${outerStyle}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <jsp:doBody />
    
</li>