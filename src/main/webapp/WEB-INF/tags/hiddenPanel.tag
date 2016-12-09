<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="id" required="true" %>

<%-- triggerValue takes precedence over triggerFunction --%>
<%@ attribute name="triggerId" %>   
<%@ attribute name="triggerValue" %>        
<%@ attribute name="triggerFunction" %>     <%-- triggerFunction takes the triggerId and returns a boolean (true = show contents/false = hide contents) --%>
<%@ attribute name="outerClass" %>
<%@ attribute name="clearOnHide" %>


<script type="text/javascript" src="<c:url value='/javascript/hiddenPanel.js' />" ></script>

<t:defaultValue value="${pageScope.clearOnHide}" defaultValue="true" var="clearOnHide" />
<t:defaultValue value="${pageScope.outerClass}" defaultValue="form-group" var="outerClass" />

<%-- triggerValue takes precedence over triggerFunction --%>
<c:if test="${not empty pageScope.triggerValue}" >
    <c:set var="triggerFunction" value="" />
</c:if>

<%-- 
    The default for a hidden panel should be block, so that it shows for JavaScript disabled browsers
    the JavaScript then initially hides the panel before any trigger events occur 
--%>
<li data-tag-type="hiddenPanel" id="${pageScope.id}" class="${pageScope.outerClass}" aria-hidden="false" style="display: block;" >
    <ul class="extra-group">
        <jsp:doBody/>
    </ul>
</li>

<c:if test="${(not empty pageScope.triggerId) && ((not empty pageScope.triggerValue) || (not empty pageScope.triggerFunction))}" >
    <script type="text/javascript">
        window.initPanelState("${pageScope.id}", "${pageScope.triggerId}", "${pageScope.triggerValue}", "${pageScope.triggerFunction}", "${clearOnHide}");
        window.initPanelEvents("${pageScope.id}", "${pageScope.triggerId}", "${pageScope.triggerValue}", "${pageScope.triggerFunction}", "${clearOnHide}");
    </script>
</c:if>
