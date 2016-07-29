<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true"%>

<%@ attribute name="id" %>
<%@ attribute name="label" %>
<%@ attribute name="labelKeyArgs" %>

<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />

<section data-tag-type="htmlsection" class="prompt e-prompt" aria-labelledby="${pageScope.id}">
    <h2 id="${pageScope.id}" class="heading-medium"><t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /></h2>
    <jsp:doBody/>
</section>
