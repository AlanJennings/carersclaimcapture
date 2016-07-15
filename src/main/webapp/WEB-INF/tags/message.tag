<%-- 
    Wrapper for spring resource bundle message
    There are a load of jsp comments in here to avoid adding a load of whitespace to the output
 
--%><%@ tag description="Message Tag" pageEncoding="UTF-8"%><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%><%--
       
--%><%@ attribute name="code" required="false" type="java.lang.String"%><%-- 
--%><%@ attribute name="parentName" required="false" type="java.lang.String"%><%--
--%><%@ attribute name="element" required="false" type="java.lang.String"%><%--
--%><c:if test="${not empty pageScope.code}"><%--
    --%><spring:message code="${pageScope.code}" text="\${${pageScope.code}}" var="messageText" /><%--
--%></c:if><%-- 
--%><c:if test="${empty pageScope.message && not empty pageScope.parentName && not empty pageScope.element}"><%-- 
    --%><spring:message code="${pageScope.parentName}.${pageScope.element}" text="" var="messageText"/><%-- 
--%></c:if><%-- 
--%><%-- 
    Don't use c:out as it escapes html, and sometimes we watn to use html
--%>${pageScope.messageText}
