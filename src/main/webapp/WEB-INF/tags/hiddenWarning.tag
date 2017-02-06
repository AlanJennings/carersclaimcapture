<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<%@ attribute name="id" required="true"%>
    
<%@ attribute name="triggerId" %>
<%@ attribute name="triggerValue" %>
<%@ attribute name="outerClass" %>
<%@ attribute name="trwrap" %>
<%@ attribute name="colspanvalue" %>

<script type="text/javascript" src="<c:url value='/javascript/hiddenPanel.js' />" ></script>

<t:defaultValue value="${pageScope.outerClass}" defaultValue="validation-summary" var="outerClass" />

<%-- 
    The default for a hidden panel should be block, so that it shows for JavaScript disabled browsers
    the JavaScript then initially hides the panel before any trigger events occur 
--%>
<c:if test="${empty trwrap}">
<div data-tag-type="hiddenWarning" id="${pageScope.id}" class="${pageScope.outerClass}" style="display: block;">
    <jsp:doBody/>
</div>
</c:if>
<c:if test="${not empty trwrap}">
<tr data-tag-type="hiddenWarning" id="${pageScope.id}" class="${pageScope.outerClass}" style="display: none;" colspan="${pageScope.colspanvalue}">
    <jsp:doBody/>
</tr>
</c:if>

<c:if test="${(not empty pageScope.triggerId) && (not empty pageScope.triggerValue)}" >
    <script type="text/javascript">
        window.initPanelState("${pageScope.id}", "${cads:encrypt(pageScope.triggerId)}", "${pageScope.triggerValue}");
        window.initPanelEvents("${pageScope.id}", "${cads:encrypt(pageScope.triggerId)}", "${pageScope.triggerValue}");
    </script>
</c:if>
