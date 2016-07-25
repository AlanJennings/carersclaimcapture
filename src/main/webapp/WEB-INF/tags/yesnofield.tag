<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" type="java.lang.String"%>
    
<%@ attribute name="id" %>
<%@ attribute name="value" %>
<%@ attribute name="yesLabelKey" %>
<%@ attribute name="noLabelKey" %>
    
<%@ attribute name="outerClass" %>
<%@ attribute name="outerStyle" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>

<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary" %>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.labelKey}" defaultValue="${pageScope.name}.label" var="labelKey" />
<t:defaultValue value="${pageScope.yesLabelKey}" defaultValue="yes" var="yesLabelKey" />
<t:defaultValue value="${pageScope.noLabelKey}" defaultValue="no" var="noLabelKey" />
<t:defaultValue value="${pageScope.outerStyle}" defaultValue="margin-bottom: 22px;" var="outerStyle" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="value" value="${requestScope[pageScope.name]}" />
</c:if>

<t:component name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">

    <fieldset class="question-group">
        <legend class="form-label-bold "><t:message code="${pageScope.labelKey}" args="${pageScope.labelKeyArgs}" /></legend>        

        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
        <ul class="inline " id="${pageScope.id}">  
            <li>
                <%-- 
                    clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control with a label
                    instead of a div makes the whole control click-able, not just the tiny checkbox in the middle 
                --%>
                <label class="block-label">
                    <input type="radio" 
                           id="${pageScope.id}_yes" 
                           name="${pageScope.name}" 
                           value="yes"  
                           <c:if test="${pageScope.value=='yes'}">checked</c:if>  
                    />
                    <span><t:message code="${pageScope.yesLabelKey}" /></span>
                </label>
            </li>
                
            <li>                                     
                <%-- 
                    clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control with a label
                    instead of a div makes the whole control click-able, not just the tiny checkbox in the middle 
                --%>            
                <label class="block-label">
                    <input type="radio" 
                           id="${pageScope.id}_no" 
                           name="${pageScope.name}" 
                           value="no"  
                           <c:if test="${pageScope.value=='no'}">checked</c:if>  
                    />
                    <span><t:message code="${pageScope.noLabelKey}" /></span>
                </label>
            </li>
        </ul>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>
    </fieldset>
    
</t:component>
