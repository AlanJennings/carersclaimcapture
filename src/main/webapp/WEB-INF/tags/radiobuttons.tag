<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true"%>
<%@ attribute name="optionValues" required="true"%> <!-- Note optionValues are white-space sensitive -->
    
<%@ attribute name="id" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="optionLabelKeys" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>
<%@ attribute name="additionalClasses" %>
<%@ attribute name="outerClass" %>
<%@ attribute name="outerStyle" %>
<%@ attribute name="value" %>
    
<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />
<t:defaultValue value="${pageScope.optionLabelKeys}" defaultValue="${pageScope.optionValues}" var="optionLabelKeys" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="value" value="${requestScope[pageScope.name]}" />
</c:if>

<t:component name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">
             
    <fieldset class="question-group">
        <legend class="form-label-bold "><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /></legend>        

        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
        <ul class="form-group form-group-compound" id="${pageScope.id}">
            <c:forTokens items="${pageScope.optionValues}" delims="|" var="optionValue" varStatus="optionValueIndex">
               <li>                
                    <%-- 
                        clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control 
                        with a label instead of a div makes the whole control click-able, not just the tiny radio button in the middle 
                    --%>
                    <label class="block-label" for="${pageScope.id}_${pageScope.optionValue}">
                        <input type="radio" 
                               id="${pageScope.id}_${pageScope.optionValue}" 
                               name="${pageScope.name}" 
                               onmousedown=""  
                               value="${pageScope.optionValue}" 
                               class="${pageScope.additionalClasses}"
                               <%-- TODO trim the optionValue before comparing --%> 
                               <c:if test="${pageScope.value==pageScope.optionValue}">checked</c:if>  
                        >
                        <%-- A bit inefficient, but less hacky than the alternatives --%>
                        <c:forTokens items="${pageScope.optionLabelKeys}" delims="|" var="optionLabelKey" varStatus="optionLabelIndex">
                            <c:if test="${pageScope.optionValueIndex.index==pageScope.optionLabelIndex.index}">
                                <span><t:message code="${pageScope.optionLabelKey}" parentName="${pageScope.name}" element="optionLabels.${pageScope.optionLabelKey}"/></span>
                            </c:if>
                        </c:forTokens>
                    </label>
                </li>
            </c:forTokens>

        </ul>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>
    </fieldset>
</t:component>
