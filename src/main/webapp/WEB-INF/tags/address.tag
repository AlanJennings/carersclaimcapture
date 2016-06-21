<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="nameOne" required="true" type="java.lang.String"%>
<%@attribute name="nameTwo" required="true" type="java.lang.String"%>
<%@attribute name="nameThree" required="true" type="java.lang.String"%>
<%@attribute name="valueOne" required="false" type="java.lang.String"%>
<%@attribute name="valueTwo" required="false" type="java.lang.String"%>
<%@attribute name="valueThree" required="false" type="java.lang.String"%>
<%@attribute name="maxlength" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
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
    <fieldset class="question-group">
        <legend class="form-label-bold">${label}</legend>
        ${hintBeforeHtml} 
        <ul id="${id}">  
            <t:textedit id="${id}_lineOne" name="${nameOne}" outerClass="form-group-compound" value="${valueOne}" maxLength="${maxLength}" /> 
            <t:textedit id="${id}_lineTwo" name="${nameTwo}" outerClass="form-group-compound" value="${valueTwo}" maxLength="${maxLength}" />
            <t:textedit id="${id}_lineThree" name="${nameThree}" outerClass="form-group-compound" value="${valueThree}" maxLength="${maxLength}" />
        </ul>
        ${hintAfterHtml} 
    </fieldset>
</li>
        
