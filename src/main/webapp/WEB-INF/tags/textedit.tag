<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="name" required="true" type="java.lang.String"%>

<%@attribute name="id" required="false" type="java.lang.String"%>
<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="outerStyle" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>
<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="maxLength" required="false" type="java.lang.String"%>
<%@attribute name="additionalClasses" required="false" type="java.lang.String"%>

<t:component name="${name}" 
             id="${id}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             label="${label}" 
             errors="${errors}">
    
    <label class="form-label-bold" for="${id}"> ${label} </label>
    <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" />
    <input type="text" class="form-control ${additionalClasses}" id="${id}" name="${name}" value="${value}" maxLength="${maxLength}" autocomplete="off">
    <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" />

</t:component>


