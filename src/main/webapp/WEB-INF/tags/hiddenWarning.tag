<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="triggerId" required="false" type="java.lang.String"%>
<%@attribute name="triggerValue" required="false" type="java.lang.String"%>
<%@attribute name="outerClass" required="false" type="java.lang.String"%>

<script type="text/javascript" src="<c:url value='/assets/javascript/hiddenPanel.js' />" ></script>

<c:if test="${empty outerClass}" >
    <c:set var="outerClass" value="validation-summary" />
</c:if>

<%-- 
    The default for a hidden panel should be block, so that it shows for JavaScript disabled browsers
    the JavaScript then initially hides the panel before any trigger events occur 
--%>
<div id="${id}" class="${outerClass}" style="display: none; margin-top: -20px;">
    <jsp:doBody/>
</div>

<c:if test="${(not empty triggerId) && (not empty triggerValue)}" >
    <script type="text/javascript">
        window.initPanelState("${id}", "${triggerId}", "${triggerValue}");
        window.initPanelEvents("${id}", "${triggerId}", "${triggerValue}");
    </script>
</c:if>
