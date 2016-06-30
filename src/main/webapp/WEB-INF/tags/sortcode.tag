<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="nameBlock1" required="true" type="java.lang.String"%>
<%@attribute name="nameBlock2" required="true" type="java.lang.String"%>
<%@attribute name="nameBlock3" required="true" type="java.lang.String"%>
<%@attribute name="valueBlock1" required="false" type="java.lang.String"%>
<%@attribute name="valueBlock2" required="false" type="java.lang.String"%>
<%@attribute name="valueBlock3" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintBeforeId" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="hintAfterId" required="false" type="java.lang.String"%>

<%@attribute name="errors" required="false" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:component name="${id}" 
             outerClass="${outerClass}" 
             outerStyle="${outerStyle}" 
             errors="${errors}">

    <fieldset class="question-group">
        <legend class="form-label-bold"> ${label} </legend>
        <t:hint hintId="${hintBeforeId}" hintText="${hintBefore}" /> 
        <%-- sort-code is just a marker class there are no styles for it --%>
        <ul class="form-date sort-code" id="${id}">
            <li class="form-group">
                <input type="tel" 
                       class="form-control"
                       id="${id}_block1"
                       name="${nameBlock1}"
                       title="Must be numbers only" 
                       value="${valueBlock1}" 
                       maxLength="2" 
                       autocomplete="off">
            </li>
            
            <li class="form-group">
                <input type="tel"
                       class="form-control"
                       id="${id}_block2"
                       name="${nameBlock2}"
                       title="Must be numbers only"
                       value="${valueBlock2}" 
                       maxLength="2"
                       autocomplete="off">
            </li>
            
            <li class="form-group">
                <input type="tel"
                       class="form-control"
                       id="${id}_block3"
                       name="${nameBlock3}"
                       title="Must be numbers only"
                       value="${valueBlock3}" 
                       maxLength="2"
                       autocomplete="off">
            </li>
        </ul>
        <t:hint hintId="${hintAfterId}" hintText="${hintAfter}" /> 
    </fieldset>

</t:component>
