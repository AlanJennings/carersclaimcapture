<%--
    Wrapper for spring resource bundle message
    There are a load of jsp comments in here to avoid adding lots of whitespace to the output
    don't put whitespace between code
--%>
<%@ tag description="Message Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="code" %>
<%@ attribute name="parentName" %>
<%@ attribute name="element" %>
<%@ attribute name="args" %>
<%--
If code is defined, then set empty value to a text representation of code, as it should exist
if args is not predefined, then load is from the messageSource by appending .args to the 'code' key,
this populates args with the value in the message properties, but does not interpret them
--%>
<c:if test="${not empty pageScope.code}">
<c:set var="emptyValue" value="\${${pageScope.code}}" />
<c:if test="${empty pageScope.args}">
<spring:message code="${pageScope.code}.args" text="" var="args" />
</c:if>
</c:if>
<%--
    If code is not populated then set code to parentName.element and use that instead, set the emptyValue
    to blank as it is optional.  Also set args to parentName.element.args and ignore args even if it is populated
--%>
<c:if test="${empty pageScope.code}">
<c:set var="emptyValue" value="" />
<c:set var="code" value="${pageScope.parentName}.${pageScope.element}" />
<spring:message code="${pageScope.code}.args" text="" var="args" />
</c:if>
<%--
    interpreted args before passing to spring, to avoid confusing it
--%>
<cads:resolveArgs var="args">${pageScope.args}</cads:resolveArgs>
<spring:message code="${pageScope.code}" text="${pageScope.emptyValue}" arguments="${pageScope.args}" argumentSeparator="|" var="messageText" />
<%--
    Don't use c:out as it escapes html, and sometimes we want to use html
    interpret the message again, to support embedding arguments directly in the message
--%>
<cads:resolveArgs>${pageScope.messageText}</cads:resolveArgs>

