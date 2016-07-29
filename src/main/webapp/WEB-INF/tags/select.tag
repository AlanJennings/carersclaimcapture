<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" type="java.lang.String"%>
<%@ attribute name="optionValues" required="true" type="java.lang.String"%> <!-- Note optionValues are white-space sensitive -->
    
<%@ attribute name="id" required="false" type="java.lang.String"%>
<%@ attribute name="labelKey" required="false" type="java.lang.String"%>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey" required="false" type="java.lang.String"%>
<%@ attribute name="hintAfterKey" required="false" type="java.lang.String"%>
<%@ attribute name="additionalClasses" required="false" type="java.lang.String"%>
<%@ attribute name="outerClass" required="false" type="java.lang.String"%>
<%@ attribute name="outerStyle" required="false" type="java.lang.String"%>
<%@ attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>
<%@ attribute name="optionLabelKeys" required="false" type="java.lang.String"%>
<%@ attribute name="value" required="false" type="java.lang.String"%>
    
<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />
<t:defaultValue value="${pageScope.optionLabelKeys}" defaultValue="${pageScope.optionValues}" var="optionLabelKeys" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="value" value="${requestScope[pageScope.name]}" />
</c:if>

<t:component tagType="select"
             name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">
    
    <label class="form-label-bold" for="${pageScope.id}"> <t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /> </label>
    <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
    
    <select id="${pageScope.id}" name="${pageScope.name}" class="form-control ${pageScope.additionalClasses}">
        <c:forTokens items="${pageScope.optionValues}" delims="|" var="optionValue" varStatus="optionValueIndex">
            <option value="${pageScope.optionValue}" 
                    <c:if test="${pageScope.value==optionValue}">selected</c:if>
            >
                <%-- A bit inefficient, but less hacky than the alternatives --%>
                <c:forTokens items="${pageScope.optionLabelKeys}" delims="|" var="optionLabel" varStatus="optionLabelIndex">
                    <c:if test="${pageScope.optionValueIndex.index==pageScope.optionLabelIndex.index}">
                        <span><t:message code="${pageScope.optionLabelKey}" parentName="${pageScope.name}" element="optionLabels.${pageScope.optionLabelKey}"/></span>
                    </c:if>
                </c:forTokens>
            </option>
        </c:forTokens>
    </select>

    <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>

</t:component>

