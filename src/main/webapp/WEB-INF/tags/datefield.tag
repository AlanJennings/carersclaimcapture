<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="nameDay" required="true" type="java.lang.String"%>
<%@attribute name="nameMonth" required="true" type="java.lang.String"%>
<%@attribute name="nameYear" required="true" type="java.lang.String"%>
<%@attribute name="valueDay" required="false" type="java.lang.String"%>
<%@attribute name="valueMonth" required="false" type="java.lang.String"%>
<%@attribute name="valueYear" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>

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

<c:if test="${not empty errors}">
    <c:set var="hasError" value="${errors.hasError(id)}" />
    <c:set var="errorMessage" value="${errors.getErrorMessage(id)}" />
</c:if>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${errorClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <!-- TODO: Probably should rearrange these a bit, so label and hints are outside the fieldset -->
    <fieldset class="question-group">
        <legend class="form-label-bold"> ${label} </legend>
        ${hintBeforeHtml}
        <ul class="form-date" id="${id}">
            <li class="form-group">
                <label for="${id}_day">Day</label>
                <input type="tel" 
                       class="form-control"
                       id="${id}_day"
                       name="${nameDay}"
                       title="Must be numbers only" 
                       value="${valueDay}" 
                       maxLength="2" 
                       autocomplete="off">
            </li>
            
            <li class="form-group month">
                <label for="${id}_month">Month</label>
                <input type="tel"
                       class="form-control"
                       id="${id}_month"
                       name="${nameMonth}"
                       title="Must be numbers only"
                       value="${valueMonth}" 
                       maxLength="2"
                       autocomplete="off">
            </li>
            
            <li class="form-group form-group-year">
                <label for="${id}_year">Year</label>
                <input type="tel"
                       class="form-control"
                       id="${id}_year"
                       name="${nameYear}"
                       title="Must be numbers only"
                       value="${valueYear}" 
                       maxLength="4"
                       autocomplete="off">
            </li>
        </ul>
        ${hintAfterHtml}
    </fieldset>

</li>
