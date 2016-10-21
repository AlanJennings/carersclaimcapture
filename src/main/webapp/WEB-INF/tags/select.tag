<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" %>
<%@ attribute name="optionValues" required="true" %> <!-- Note optionValues are white-space sensitive -->
    
<%@ attribute name="id"%>
<%@ attribute name="labelKey"%>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey"%>
<%@ attribute name="hintAfterKey"%>
<%@ attribute name="additionalClasses"%>
<%@ attribute name="outerClass"%>
<%@ attribute name="outerStyle"%>
<%@ attribute name="optionLabelKeys"%>
<%@ attribute name="value"%>
<%@ attribute name="includeBlank" %>

<%@ attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.validations.ValidationSummary"%>
    
<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />
<t:defaultValue value="${pageScope.optionLabelKeys}" defaultValue="${pageScope.optionValues}" var="optionLabelKeys" />
<t:defaultValue value="${pageScope.includeBlank}" defaultValue="false" var="includeBlank" />


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
        <c:if test="${pageScope.includeBlank='true'}" >
            <option value="">Select</option>
        </c:if>
        
        <c:forTokens items="${pageScope.optionValues}" delims="|" var="optionValue" varStatus="optionValueIndex">
            <option value="${pageScope.optionValue}" 
                <c:if test="${pageScope.value==optionValue}">selected</c:if>
            >
                <%-- 
                     both lists use the same ordering, but we can't access the element directly as it is a string
                     not an array, so we can iterate over the labels using c:forTokens until the index matches the 
                     value index.  A bit inefficient, but less hacky than the alternatives.
                --%>
                <c:forTokens items="${pageScope.optionLabelKeys}" delims="|" var="optionLabelKey" varStatus="optionLabelIndex">
                    <c:if test="${pageScope.optionValueIndex.index==pageScope.optionLabelIndex.index}">
                        <span><t:message parentName="${pageScope.name}" element="optionLabels.${pageScope.optionLabelKey}"/></span>
                    </c:if>
                </c:forTokens>
            </option>
        </c:forTokens>
    </select>

    <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>

</t:component>

