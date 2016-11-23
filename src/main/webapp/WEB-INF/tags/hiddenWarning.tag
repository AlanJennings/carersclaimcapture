<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="id" required="true"%>
    
<%@ attribute name="triggerId" %>
<%@ attribute name="triggerValue" %>
<%@ attribute name="outerClass" %>

<script type="text/javascript" src="<c:url value='/javascript/hiddenPanel.js' />" ></script>

<t:defaultValue value="${pageScope.outerClass}" defaultValue="validation-summary" var="outerClass" />

<%-- 
    The default for a hidden panel should be block, so that it shows for JavaScript disabled browsers
    the JavaScript then initially hides the panel before any trigger events occur 
--%>
<div data-tag-type="hiddenWarning" id="${pageScope.id}" class="${pageScope.outerClass}" style="display: none; margin-top: -20px;">
    <jsp:doBody/>
</div>

<c:if test="${(not empty pageScope.triggerId) && (not empty pageScope.triggerValue)}" >
    <script type="text/javascript">
        window.initPanelState("${pageScope.id}", "${pageScope.triggerId}", "${pageScope.triggerValue}");
        window.initPanelEvents("${pageScope.id}", "${pageScope.triggerId}", "${pageScope.triggerValue}");
    </script>
</c:if>
