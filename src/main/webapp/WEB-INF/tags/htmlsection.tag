<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>

<section class="prompt e-prompt" aria-labelledby="${id}">
    <h2 id="${id}" class="heading-medium">${label}</h2>
    <jsp:doBody/>
</section>
