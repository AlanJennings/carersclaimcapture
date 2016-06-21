<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="checkedValue" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="text" required="false" type="java.lang.String"%>
<%@attribute name="additionalClasses" required="false" type="java.lang.String"%>
<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="blockLabel" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<c:if test="${not empty hintBefore}">
    <c:if test="${empty hintBeforeId}">
        <c:set var="hintBeforeHtml" value="<p class='form-hint'>${hintBefore}</p>" />
    </c:if>
    <c:if test="${not empty hintBeforeId}">
        <c:set var="hintBeforeHtml" value="<p class='form-hint' id='${hintBeforeId}'>${hintBefore}</p>" />
    </c:if>
</c:if>

<c:if test="${not empty hintAfterHtml}">
    <c:if test="${empty hintAfterId}">
        <c:set var="hintAfterHtml" value="<p class='form-hint'>${hintAfter}</p>" />
    </c:if>
    <c:if test="${not empty hintAfterId}">
        <c:set var="hintAfterHtml" value="<p class='form-hint' id='${hintAfterId}'>${hintAfter}</p>" />
    </c:if>
</c:if>

<%-- default, unless specifically turned off --%>
<c:if test="${blockLabel != 'false'}">
    <c:set var="labelClass" value=" block-label" />
</c:if>

<c:if test="${not empty errors}">
    <c:set var="hasError" value="${errors.hasError(name)}" />
    <c:set var="errorMessage" value="${errors.getErrorMessage(name)}" />
</c:if>

<c:if test="${empty outerClass}">
    <c:set var="outerClass" value="form-group" />
</c:if>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>

<li class="<c:out value='${outerClass}'/> <c:out value='${errorClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    ${hintBeforeHtml} 
    
    <c:if test="${value==checkedValue}" >
        <c:set var="checked" value="checked" />
    </c:if>
    <label for="${id}" class="${labelClass}"> 
        <span class="form-label-bold">${label}</span>
        <input type="checkbox" id="${id}" name="${name}" value="${checkedValue}" ${checked} style="top: 24px;">
        <span>${text}</span>
    </label>
    
    ${hintAfterHtml}
</li>
