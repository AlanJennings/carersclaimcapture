<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.benefits" currentPage="${currentPage}">
    
    <t:pageContent errors="${validationErrors}" page="page.benefits">
        
        <t:radiobuttons name="benefitsAnswer" optionValues="PIP|DLA|AA|CAA|AFIP|NONE" />
        
        <t:hiddenWarning id="answerNoMessageWrap" triggerId="benefitsAnswer" triggerValue="NONE">
            <p>You'll only get Carer's Allowance if the person you care for gets one of these benefits.</p>
        </t:hiddenWarning>
    
    </t:pageContent>
    
</t:mainPage>
