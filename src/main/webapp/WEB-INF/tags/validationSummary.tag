<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@attribute name="errors" required="true" type="uk.gov.dwp.carersallowance.validations.ValidationSummary" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${pageScope.errors.hasFormErrors()}">
    <div class="validation-summary">
        <h2 class="heading-small"><t:message code="error.check.form" /></h2>
        <p><t:message code="error.fix.following" /></p>
        <ol class="list-bullet">
            <c:forEach var="error" items="${pageScope.errors.formErrors}" varStatus="status">
                <li>
                    <c:choose>
                        <c:when test="${fn:contains(pageScope.error.displayName, '.label}')}">
                            <a href="#<c:out value="${pageScope.error.id}"/>"><c:out value="${pageScope.error.errorMessage}"/></a>
                        </c:when>
                        <c:otherwise>
                            <a href="#<c:out value="${pageScope.error.id}"/>"><c:out value="${pageScope.error.displayName}"/> - <c:out value="${pageScope.error.errorMessage}"/></a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ol>
    </div>
</c:if>