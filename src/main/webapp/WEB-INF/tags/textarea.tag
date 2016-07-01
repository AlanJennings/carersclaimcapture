<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>

<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="maxLength" required="false" type="java.lang.String"%>
<%@attribute name="additionalClasses" required="false" type="java.lang.String"%>
<%@attribute name="showRemainingChars" required="false" type="java.lang.String"%>

<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<script type="text/javascript" src="<c:url value='/assets/javascript/textAreaCounter.js' />" ></script>

<c:if test="${empty showRemainingChars}" >
    <c:set var="showRemainingChars" value="false" />
</c:if>

<%-- Override showRemainingChars if no maxlength has been set --%>
<c:if test="${empty maxLength}" >
    <c:set var="showRemainingChars" value="false" />
</c:if>

<t:component name="${name}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             errors="${errors}">

        <label class="form-label-bold" for="${id}"> ${label} </label>
        <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" /> 
        <textarea class="form-control ${additionalClasses}" id="${id}" name="${name}" maxLength="${maxLength}" >${value}</textarea>
        <c:if test="${showRemainingChars=='true'}" >
            <c:set var="remainingChars" value="${maxLength - fn:length(value)}" />
            <p class="form-hint countdown">${remainingChars} characters left</p>
        </c:if>
        <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" />

</t:component>

<c:if test="${(not empty showRemainingChars)}" >
    <script type="text/javascript">
        window.areaCounter("${id}", Number("${maxLength}"));
    </script>
</c:if>

