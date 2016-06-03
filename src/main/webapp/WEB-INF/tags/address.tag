<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="nameOne" required="true" type="java.lang.String"%>
<%@attribute name="nameTwo" required="true" type="java.lang.String"%>
<%@attribute name="nameThree" required="true" type="java.lang.String"%>
<%@attribute name="valueOne" required="false" type="java.lang.String"%>
<%@attribute name="valueTwo" required="false" type="java.lang.String"%>
<%@attribute name="valueThree" required="false" type="java.lang.String"%>
<%@attribute name="maxlength" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>

<%@attribute name="hasError" required="false" type="java.lang.Object"%>
<%@attribute name="errorMessage" required="false" type="java.lang.String"%>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${errorClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>    
    <fieldset class="question-group">
        <legend class="form-label-bold">${label}</legend>
        ${hintBefore} 
        <ul id="${id}">  
            <t:textedit id="${id}_lineOne" name="${nameOne}" outerClass="form-group-compound" value="${valueOne}" maxLength="${maxLength}" /> 
            <t:textedit id="${id}_lineTwo" name="${nameTwo}" outerClass="form-group-compound" value="${valueTwo}" maxLength="${maxLength}" />
            <t:textedit id="${id}_lineThree" name="${nameThree}" outerClass="form-group-compound" value="${valueThree}" maxLength="${maxLength}" />
        </ul>
        ${hintAfter} 
    </fieldset>
</li>
        
