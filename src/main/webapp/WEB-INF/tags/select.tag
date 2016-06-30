<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="name" required="true" type="java.lang.String"%>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>
<%@attribute name="additionalClasses" required="false" type="java.lang.String"%>
<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="outerStyle" required="false" type="java.lang.String"%>
<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<%@attribute name="optionValues" required="true" type="java.lang.String"%> <!-- Note optionValues are white-space sensitive -->
<%@attribute name="optionLabels" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>

<t:component name="${name}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             errors="${errors}">
    
    <label class="form-label-bold" for="${id}"> ${label} </label>
    <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" />
    
    <select id="${id}" name="${name}" class="form-control ${additionalClasses}">
        <c:forTokens items="${optionValues}" delims="|" var="optionValue" varStatus="optionValueIndex">
            <option value="${optionValue}" 
                    <c:if test="${value==optionValue}">selected</c:if>
            >
                <%-- A bit inefficient, but less hacky than the alternatives --%>
                <c:forTokens items="${optionLabels}" delims="|" var="optionLabel" varStatus="optionLabelIndex">
                    <c:if test="${optionValueIndex.index==optionLabelIndex.index}">
                        <span>${optionLabel}</span>
                    </c:if>
                </c:forTokens>
            </option>
        </c:forTokens>
    </select>

    <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" />

</t:component>

