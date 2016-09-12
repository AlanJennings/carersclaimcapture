<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.disclaimer" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.disclaimer" nextButtonTextKey="page.disclaimer.nextButtonText" backLink="${previousPage}">
        
        <t:htmlsection name="disclaimer.section1">
            <p><t:message code="disclaimer.section1.line1" /></p>
        </t:htmlsection>

        <t:htmlsection name="disclaimer.section2"> 
            <ul class="list-bullet">
                <li><t:message code="disclaimer.section2.line1" /></li>
                <li><t:message code="disclaimer.section2.line2" /></li>
                <li><t:message code="disclaimer.section2.line3" />
                    <a rel="external" 
                       href="https://www.gov.uk/find-your-local-council" 
                       target="_blank" 
                    ><t:message code="disclaimer.section2.line4" /></a> 
                    <t:message code="disclaimer.section2.line5" />
                </li>
            </ul>
        </t:htmlsection>

        <t:htmlsection name="disclaimer.section3">
            <p><t:message code="disclaimer.section3.line1" />
                <a rel="external" 
                   href="https://www.gov.uk/carers-allowance/what-youll-get" 
                   target="_blank"
                ><t:message code="disclaimer.section3.line2" /></a>.
            </p>
        </t:htmlsection>
    
        <t:htmlsection name="disclaimer.section4"><p><t:message code="disclaimer.section4.line1" /></p></t:htmlsection>
    
    </t:pageContent>

</t:mainPage>                