<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>

<%@attribute name="yesLabel" required="false" type="java.lang.String"%>
<%@attribute name="noLabel" required="false" type="java.lang.String"%>

<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="outerStyle" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<%-- set default values for yes & no labels --%>
<c:if test="${empty yesLabel}">
    <c:set var="yesLabel" value="Yes" />
</c:if>
<c:if test="${empty noLabel}">
    <c:set var="noLabel" value="No" />
</c:if>

<c:if test="${empty outerStyle}" >
    <c:set var="outerStyle" value="margin-bottom: 22px;" />
</c:if>

<t:component name="${name}" 
             id="${id}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             label="${label}" 
             errors="${errors}">

    <fieldset class="question-group">
        <legend class="form-label-bold ">${label}</legend>        

        <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" /> 
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
        <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" /> 
    </fieldset>
    
</t:component>
