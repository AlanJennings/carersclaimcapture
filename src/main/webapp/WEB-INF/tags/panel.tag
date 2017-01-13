<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="id" required="true" %>

<%@ attribute name="labelKey" %>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="innerClass" %>

<t:defaultValue value="${pageScope.innerClass}" defaultValue="break-data" var="innerClass" />

<fieldset data-tag-type="panel" id="${cads:encrypt(pageScope.id)}" class="form-elements results-detail">
    <legend class="heading-medium form-class-bold" id="${cads:encrypt(pageScope.id)}_label"><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /></legend>
    <div class="data-table">
        <ul class="${pageScope.innerClass}">
            <jsp:doBody/>
        </ul>
    </div>
</fieldset>


