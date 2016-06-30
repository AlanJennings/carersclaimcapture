<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="name" required="true" type="java.lang.String"%>

<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="outerStyle" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>
<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:component name="${name}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             errors="${errors}">
             
    <fieldset class="question-group">
        <legend class="form-label-bold ">${label}</legend>        

        <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" /> 
        <ul class="form-group form-group-compound" id="${id}">
            <jsp:doBody />
        </ul>
        <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" /> 
    </fieldset>
</t:component>

