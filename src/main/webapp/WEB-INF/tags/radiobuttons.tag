<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="optionValues" required="true" type="java.lang.String"%> <!-- Note optionValues are white-space sensitive -->
<%@attribute name="optionLabels" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="warningText" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<c:if test="${not empty errors}">
    <c:set var="hasError" value="${errors.hasError(name)}" />
    <c:set var="errorMessage" value="${errors.getErrorMessage(name)}" />
</c:if>

<%-- TODO add track events separately using jquery --%>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${errorClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <fieldset class="question-group">
        <legend class="form-label-bold ">${label}</legend>        

        ${hintBefore}
        <ul class="form-group form-group-compound" id="${id}">
            <c:forTokens items="${optionValues}" delims="|" var="optionValue" varStatus="optionValueIndex">
               <li>                
                    <%-- 
                        clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control 
                        with a label instead of a div makes the whole control click-able, not just the tiny radio button in the middle 
                    --%>
                    <label class="block-label" for="${id}_${optionValue}">
                        <input type="radio" 
                               id="${id}_${optionValue}" 
                               name="${name}" 
                               onmousedown=""  
                               value="${optionValue}"  
                               <c:if test="${value==optionValue}">checked</c:if>  
                        >
                        <%-- A bit inefficient, but less hacky than the alternatives --%>
                        <c:forTokens items="${optionLabels}" delims="|" var="optionLabel" varStatus="optionLabelIndex">
                            <c:if test="${optionValueIndex.index==optionLabelIndex.index}">
                                <span>${optionLabel}</span>
                            </c:if>
                        </c:forTokens>
                    </label>
                </li>
            </c:forTokens>

        </ul>
        ${hintAfter}
    </fieldset>
</li>
