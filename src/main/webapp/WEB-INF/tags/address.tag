<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<%@ attribute name="name" required="true" %>

<%@ attribute name="id" %>
<%@ attribute name="nameOne" %>
<%@ attribute name="nameTwo" %>
<%@ attribute name="nameThree" %>
<%@ attribute name="valueOne" %>
<%@ attribute name="valueTwo" %>
<%@ attribute name="valueThree" %>
<%@ attribute name="useRawValue" %>
<%@ attribute name="maxlength" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>

<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.validations.ValidationSummary"%>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />

<c:if test="${empty pageScope.nameOne}">
    <c:set var="nameOne" value="${pageScope.name}LineOne" />
    <c:set var="nameTwo" value="${pageScope.name}LineTwo" />
    <c:set var="nameThree" value="${pageScope.name}LineThree" />
</c:if>

<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="valueOne" value="${requestScope[pageScope.nameOne]}" />
    <c:set var="valueTwo" value="${requestScope[pageScope.nameTwo]}" />
    <c:set var="valueThree" value="${requestScope[pageScope.nameThree]}" />
</c:if>

<t:component tagType="address"
             name="${pageScope.id}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">
                
    <fieldset class="question-group">
        <legend class="form-label-bold" id="${cads:encrypt(pageScope.id)}_label"><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /></legend>
        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
        <ul id="${cads:encrypt(pageScope.id)}">
            <t:textedit id="${pageScope.id}_lineOne" name="${pageScope.nameOne}" outerClass="form-group-compound" value="${pageScope.valueOne}" maxLength="${pageScope.maxLength}" tagNested="true" /> 
            <t:textedit id="${pageScope.id}_lineTwo" name="${pageScope.nameTwo}" outerClass="form-group-compound" value="${pageScope.valueTwo}" maxLength="${pageScope.maxLength}" tagNested="true" />
            <t:textedit id="${pageScope.id}_lineThree" name="${pageScope.nameThree}" outerClass="form-group-compound" value="${pageScope.valueThree}" maxLength="${pageScope.maxLength}" tagNested="true" />
        </ul>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter" />
    </fieldset>
</t:component>
        
