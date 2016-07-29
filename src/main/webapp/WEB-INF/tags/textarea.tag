<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" %>
    
<%@ attribute name="id" %>
<%@ attribute name="value" %>
<%@ attribute name="maxLength" %>
<%@ attribute name="additionalClasses" %>
<%@ attribute name="showRemainingChars" %>
<%@ attribute name="outerClass" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>

<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<script type="text/javascript" src="<c:url value='/assets/javascript/textAreaCounter.js' />" ></script>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />
<t:defaultValue value="${pageScope.showRemainingChars}" defaultValue="false" var="showRemainingChars" />
<t:defaultValue value="${pageScope.maxLength}" defaultValue="false" var="showRemainingChars" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="value" value="${requestScope[pageScope.name]}" />
</c:if>

<t:component tagType="message"
             name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">

        <label class="form-label-bold" for="${pageScope.id}"> <t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /> </label>
        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/> 
        <textarea class="form-control ${pageScope.additionalClasses}" id="${pageScope.id}" name="${pageScope.name}" maxLength="${pageScope.maxLength}" >${pageScope.value}</textarea>
        <c:if test="${pageScope.showRemainingChars=='true'}" >
            <c:set var="remainingChars" value="${pageScope.maxLength - fn:length(pageScope.value)}" />
            <p class="form-hint countdown">${pageScope.remainingChars} characters left</p>
        </c:if>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>

</t:component>

<c:if test="${pageScope.showRemainingChars=='true'}" >    
    <script type="text/javascript">
        window.areaCounter("${id}", Number("${pageScope.maxLength}"));
    </script>
</c:if>

