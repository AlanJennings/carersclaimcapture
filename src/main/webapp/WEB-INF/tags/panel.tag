<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="innerClass" required="false" type="java.lang.String"%>

<c:if test="${empty innerClass}">
    <c:set var="innerClass" value="break-data" />
</c:if>


<fieldset id="${id}" class="form-elements results-detail">
    <legend class="heading-medium form-class-bold">${label}</legend>
    <div class="data-table">
        <ul class="${innerClass}">
            <jsp:doBody/>
        </ul>
    </div>
</fieldset>


