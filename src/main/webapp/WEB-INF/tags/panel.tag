<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>

<fieldset id="${id}" class="form-elements results-detail">
    <legend class="heading-medium form-class-bold">${label}</legend>
    <div class="data-table">
        <ul class="break-data">
            <jsp:doBody/>
        </ul>
    </div>
</fieldset>


