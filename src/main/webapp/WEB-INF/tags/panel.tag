<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="id" required="true" %>

<%@ attribute name="labelKey" %>
<%@ attribute name="innerClass" %>

<t:defaultValue value="${pageScope.innerClass}" defaultValue="break-data" var="innerClass" />

<fieldset id="${pageScope.id}" class="form-elements results-detail">
    <legend class="heading-medium form-class-bold"><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label"/></legend>
    <div class="data-table">
        <ul class="${pageScope.innerClass}">
            <jsp:doBody/>
        </ul>
    </div>
</fieldset>


