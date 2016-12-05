<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    
    <t:pageContent errors="${validationErrors}">
        
        <t:radiobuttons name="benefitsAnswer" optionValues="PIP|DLA|AA|CAA|AFIP|NONE" />
        
        <t:hiddenWarning id="answerNoMessageWrap" triggerId="benefitsAnswer" triggerValue="NONE">
            <p><t:message code="benefitsAnswer.warning.message" /></p>
        </t:hiddenWarning>
    
    </t:pageContent>
    
</t:mainPage>

