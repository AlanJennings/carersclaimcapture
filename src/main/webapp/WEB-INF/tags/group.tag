<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true" type="java.lang.String"%>
    
<%@ attribute name="outerClass" %>
<%@ attribute name="outerStyle" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>

<%@ attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:component name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">
             
    <fieldset class="question-group">
        
        <legend class="form-label-bold "><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label"/></legend>        
        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
        <ul class="form-group form-group-compound" id="${pageScope.id}">
            <jsp:doBody />
        </ul>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>
    
    </fieldset>
</t:component>

