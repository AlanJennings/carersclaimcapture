<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="yesLabel" required="false" type="java.lang.String"%>
<%@attribute name="noLabel" required="false" type="java.lang.String"%>

<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeHtml" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterHtml" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<c:if test="${not empty errors}">
    <c:set var="hasError" value="${errors.hasError(name)}" />
    <c:set var="errorMessage" value="${errors.getErrorMessage(name)}" />
</c:if>

<!-- TODO add track events separately using jquery -->


<%-- set default values for yes & no labels --%>
<c:if test="${empty yesLabel}">
    <c:set var="yesLabel" value="Yes" />
</c:if>
<c:if test="${empty noLabel}">
    <c:set var="noLabel" value="No" />
</c:if>

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

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${errorClass}'/>" style="margin-bottom: 22px;">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <fieldset class="question-group">
        <legend class="form-label-bold ">${label}</legend>        

        ${hintBeforeHtml}
        <ul class="inline " id="${id}">  
            <li>
                <%-- 
                    clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control with a label
                    instead of a div makes the whole control click-able, not just the tiny checkbox in the middle 
                --%>
                <label class="block-label">
                    <input type="radio" 
                           id="${id}_yes" 
                           name="${name}" 
                           value="yes"  
                           <c:if test="${value=='yes'}">checked</c:if>  
                    />
                    <span><c:out value="${yesLabel}" /></span>
                </label>
            </li>
                
            <li>                                     
                <%-- 
                    clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control with a label
                    instead of a div makes the whole control click-able, not just the tiny checkbox in the middle 
                --%>            
                <label class="block-label">
                    <input type="radio" 
                           id="${id}_no" 
                           name="${name}" 
                           value="no"  
                           <c:if test="${value=='no'}">checked</c:if>  
                    />
                    <span><c:out value="${noLabel}" /></span>
                </label>
            </li>
        </ul>
        ${hintAfterHtml}
    </fieldset>
</li>
