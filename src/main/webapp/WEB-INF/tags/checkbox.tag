<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<%@ attribute name="name" required="true" %>

<%@ attribute name="id" %>
<%@ attribute name="checkedValue" %>
<%@ attribute name="useRawValue" %>
<%@ attribute name="value" %>
<%@ attribute name="textKey" %>
<%@ attribute name="additionalClasses" %>
<%@ attribute name="outerStyle" %>
<%@ attribute name="outerClass" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="blockLabel" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>

<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.validations.ValidationSummary"%>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />
<t:defaultValue value="${pageScope.checkedValue}" defaultValue="yes" var="checkedValue" />
<t:defaultValue value="${pageScope.textKey}" defaultValue="${pageScope.name}.text" var="textKey" />

<%-- default, unless specifically turned off --%>
<c:if test="${pageScope.blockLabel != 'false'}">
    <c:set var="labelClass" value=" block-label" />
</c:if>

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="value" value="${requestScope[pageScope.name]}" />
</c:if>

<t:component tagType="checkbox"
             name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">

    <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
    
    <c:if test="${pageScope.value==pageScope.checkedValue}" >
        <c:set var="checked" value="checked" />
    </c:if>
    
    <label for="${cads:encrypt(pageScope.id)}" class="${pageScope.labelClass}" id="${pageScope.id}_label">
        <span class="form-label-bold"><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" /></span>
        <input type="checkbox" id="${cads:encrypt(pageScope.id)}" name="${cads:encrypt(pageScope.name)}" class="${pageScope.additionalClasses}" value="${pageScope.checkedValue}" ${pageScope.checked} style="top: 24px;">
        <span><t:message code="${pageScope.textKey}" /></span>
    </label>
    
    <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>
</t:component>
