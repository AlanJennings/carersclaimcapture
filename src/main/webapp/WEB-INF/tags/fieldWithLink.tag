<%@ tag description="Preview field link Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="id" %>
<%@ attribute name="name" %>
<%@ attribute name="value" %>
<%@ attribute name="displayChangeButton" %>
<%@ attribute name="link" %>

<tr class="review-change" id="${id}">
	<td id="${id}_label" class="review-label">
		<b><t:message code="${name}" /></b>
	</td>
	<td id="${id}_value" class="review-value">
        <t:message code="${value}" />
	</td>
	<td id="${id}_linkContainer" class="review-action hide-print">
		<c:if test="${displayChangeButton eq \"true\"}" >
            <a class="previewChangeLink secondary" id="${id}_link" href="${link}" onclick>
                <t:message code="preview.change" />
                <span class="visuallyhidden"><t:message code="${name}" /> (optional)</span>
            </a>
		</c:if>
	</td>
</tr>
