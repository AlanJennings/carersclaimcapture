<%@ tag description="Accordion Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="label" %>
<%@ attribute name="openLabel" %>
<%@ attribute name="closeLabel" %>
<%@ attribute name="track" %>

<div class="accordion accordion-open">
    <h2 class="heading-medium accordion-title" accordion-track="${openLabel}" onmousedown="if($(this).attr('accordion-track') === '${openLabel}'){ <c:if test="${track}">trackEvent('${closeLabel}');</c:if> $(this).attr('accordion-track', '${closeLabel}'); } else { <c:if test="${track}">trackEvent('${openLabel}');</c:if> $(this).attr('accordion-track', '${openLabel}'); }"><t:message code="${label}" /></h2>
    <div class="accordion-content">
        <table class="review">
            <jsp:doBody />
        </table>
    </div>
</div>
