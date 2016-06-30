<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

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
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<%-- default, unless specifically turned off --%>
<c:if test="${blockLabel != 'false'}">
    <c:set var="labelClass" value=" block-label" />
</c:if>

<t:component name="${name}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             errors="${errors}">

    <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" />  
    
    <c:if test="${value==checkedValue}" >
        <c:set var="checked" value="checked" />
    </c:if>
    <label for="${id}" class="${labelClass}"> 
        <span class="form-label-bold">${label}</span>
        <input type="checkbox" id="${id}" name="${name}" class="${additionalClasses}" value="${checkedValue}" ${checked} style="top: 24px;">
        <span>${text}</span>
    </label>
    
    <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" /> 
</t:component>
