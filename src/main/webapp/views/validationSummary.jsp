<%@ page language="java" isELIgnored ="false" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${errors.hasFormErrors()}" >
    <div class="validation-summary">
        <h2 class="heading-small">Check the form</h2>
        <p>Fix the following:</p>
        <ol class="list-bullet">
            <c:forEach var="error" items="${errors.formErrors}" varStatus="status">
                <li>
                    <a href="#<c:out value="${error.id}"/>"><c:out value="${error.displayName}"/> - <c:out value="${error.errorMessage}"/></a>
                </li>
            </c:forEach>
        </ol>
    </div>
</c:if>