<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="maxLength" required="false" type="java.lang.String"%>
<%@attribute name="additionalClasses" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>

<%@attribute name="hasError" required="false" type="java.lang.Object"%>
<%@attribute name="errorMessage" required="false" type="java.lang.String"%>

<c:if test="${hasError}" >
    <c:set var="additionalClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${additionalClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <label class="form-label-bold" for="${id}"> ${label} </label>
    ${hintBefore} 
    <input type="text" class="form-control ${additionalClasses}" id="${id}" name="${name}" value="${value}" maxLength="${maxLength}" autocomplete="off">
    ${hintAfter}

</li>
