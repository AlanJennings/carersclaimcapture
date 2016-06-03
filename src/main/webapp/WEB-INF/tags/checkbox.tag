<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="checkedValue" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="text" required="false" type="java.lang.String"%>
<%@attribute name="additionalClasses" required="false" type="java.lang.String"%>
<%@attribute name="outerClass" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>

<%@attribute name="hasError" required="false" type="java.lang.Object"%>
<%@attribute name="errorMessage" required="false" type="java.lang.String"%>

<c:if test="${empty outerClass}">
    <c:set var="outerClass" value="form-group" />
</c:if>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>

<li class="<c:out value='${outerClass}'/> <c:out value='${errorClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    ${hintBefore} 
    
    <c:if test="${value=checkedValue}" >
        <c:set var="checked" value="checked" />
    </c:if>
    <label for="${id}"> 
        <span class="form-label-bold">${label} </span>
        <input type="checkbox" id="${id}" name="${name}" value="${checkedValue}">
        <span>${text}</span>
    </label>
    
    ${hintAfter}
</li>
