<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" nextButtonTextKey="page.disclaimer.nextButtonText" backLink="${previousPage}">
        
        <t:htmlsection name="disclaimer.section1">
            <p><t:message code="disclaimer.section1.line1" /></p>
        </t:htmlsection>

        <t:htmlsection name="disclaimer.section2"> 
            <ul class="list-bullet">
                <li><t:message code="disclaimer.section2.line1" /></li>
                <li><t:message code="disclaimer.section2.line2" /></li>
                <li><t:message code="disclaimer.section2.line3" /></li>
            </ul>
        </t:htmlsection>

        <t:htmlsection name="disclaimer.section3">
            <p><t:message code="disclaimer.section3.line1" /></p>
        </t:htmlsection>
    
        <t:htmlsection name="disclaimer.section4"><p><t:message code="disclaimer.section4.line1" /></p></t:htmlsection>
    
    </t:pageContent>

</t:mainPage>                