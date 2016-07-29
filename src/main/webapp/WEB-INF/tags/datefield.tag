<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" %>
    
<%@ attribute name="id" %>
<%@ attribute name="nameDay" %>
<%@ attribute name="nameMonth" %>
<%@ attribute name="nameYear" %>
<%@ attribute name="valueDay" %>
<%@ attribute name="valueMonth" %>
<%@ attribute name="valueYear" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>
    
<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${empty pageScope.nameDay}">
    <c:set var="nameDay" value="${pageScope.name}_day" />
    <c:set var="nameMonth" value="${pageScope.name}_month" />
    <c:set var="nameYear" value="${pageScope.name}_year" />
</c:if>

<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="valueDay" value="${requestScope[pageScope.nameDay]}" />
    <c:set var="valueMonth" value="${requestScope[pageScope.nameMonth]}" />
    <c:set var="valueYear" value="${requestScope[pageScope.nameYear]}" />
</c:if>

<c:set var="numbersWarning"><t:message code='warning.numbers.only' /></c:set>

<t:component tagType="datefield"
             name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">

    <!-- TODO: Probably should rearrange these a bit, so label and hints are outside the fieldset -->
    <fieldset class="question-group">
        <legend class="form-label-bold"> <t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /> </legend>
        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore" />
        <ul class="form-date" id="${pageScope.id}">
            <li class="form-group">
                <label for="${pageScope.id}_day"><t:message code="day" /></label>
                <input type="tel" 
                       class="form-control"
                       id="${pageScope.id}_day"
                       name="${pageScope.nameDay}"
                       title="${pageScope.numbersWarning}" 
                       value="${pageScope.valueDay}" 
                       maxLength="2" 
                       autocomplete="off">
            </li>
            
            <li class="form-group month">
                <label for="${pageScope.id}_month"><t:message code="month" /></label>
                <input type="tel"
                       class="form-control"
                       id="${pageScope.id}_month"
                       name="${pageScope.nameMonth}"
                       title="${pageScope.numbersWarning}"
                       value="${pageScope.valueMonth}" 
                       maxLength="2"
                       autocomplete="off">
            </li>
            
            <li class="form-group form-group-year">
                <label for="${pageScope.id}_year"><t:message code="year" /></label>
                <input type="tel"
                       class="form-control"
                       id="${pageScope.id}_year"
                       name="${pageScope.nameYear}"
                       title="${pageScope.numbersWarning}"
                       value="${pageScope.valueYear}" 
                       maxLength="4"
                       autocomplete="off">
            </li>
        </ul>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter" />
    </fieldset>

</t:component>
