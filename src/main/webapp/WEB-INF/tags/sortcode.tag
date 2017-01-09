<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>
    
<%@ attribute name="name" required="true" %>
    
<%@ attribute name="id" %>
<%@ attribute name="nameBlock1" %>
<%@ attribute name="nameBlock2" %>
<%@ attribute name="nameBlock3" %>
<%@ attribute name="valueBlock1" %>
<%@ attribute name="valueBlock2" %>
<%@ attribute name="valueBlock3" %>
<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey" %>
<%@ attribute name="hintAfterKey" %>

<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.validations.ValidationSummary"%>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${empty pageScope.nameBlock1}">
    <c:set var="nameBlock1" value="${pageScope.name}_1" />
    <c:set var="nameBlock2" value="${pageScope.name}_2" />
    <c:set var="nameBlock3" value="${pageScope.name}_3" />
</c:if>

<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="valueBlock1" value="${requestScope[pageScope.nameBlock1]}" />
    <c:set var="valueBlock2" value="${requestScope[pageScope.nameBlock2]}" />
    <c:set var="valueBlock3" value="${requestScope[pageScope.nameBlock3]}" />
</c:if>

<c:set var="numbersWarning"><t:message code='warning.numbers.only' /></c:set>
<c:set var='id1' value='${pageScope.id}_1' />
<c:set var='id2' value='${pageScope.id}_2' />
<c:set var='id3' value='${pageScope.id}_3' />


<t:component tagType="sortcode"
             name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">

    <fieldset class="question-group">
        <legend class="form-label-bold"> <t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /> </legend>
        <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
        <%-- sort-code is just a marker class there are no styles for it --%>
        <ul class="form-date sort-code" id="${cads:encrypt(pageScope.id)}">
            <li class="form-group">
                <input type="tel" 
                       class="form-control"
                       id="${cads:encrypt(id1)}"
                       name="${cads:encrypt(pageScope.nameBlock1)}"
                       title="${pageScope.numbersWarning}" 
                       value="${pageScope.valueBlock1}" 
                       maxLength="2" 
                       autocomplete="off">
            </li>
            
            <li class="form-group">
                <input type="tel"
                       class="form-control"
                       id="${cads:encrypt(id2)}"
                       name="${cads:encrypt(pageScope.nameBlock2)}"
                       title="${pageScope.numbersWarning}"
                       value="${pageScope.valueBlock2}" 
                       maxLength="2"
                       autocomplete="off">
            </li>
            
            <li class="form-group">
                <input type="tel"
                       class="form-control"
                       id="${cads:encrypt(id3)}"
                       name="${cads:encrypt(pageScope.nameBlock3)}"
                       title="${pageScope.numbersWarning}"
                       value="${pageScope.valueBlock3}" 
                       maxLength="2"
                       autocomplete="off">
            </li>
        </ul>
        <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter"/>
    </fieldset>

</t:component>
