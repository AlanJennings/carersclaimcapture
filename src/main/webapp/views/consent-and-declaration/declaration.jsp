<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.declaration" backLink="${previousPage}">
        
        <t:yesnofield name="tellUsWhyInformation" />
        
        <t:hiddenPanel id="tellUsWhyInformationWrap" triggerId="tellUsWhyInformation" triggerValue="no" >
            <t:textarea name="tellUsWhyPerson" maxLength="3000" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:htmlsection name="declarationText">
            <p><t:message code="declarationText.line1" /></p>
            <ul class="list-bullet">
                <li><t:message code="declarationText.line2" /></li>
                
                    <li><t:message code="declarationText.line3" /></li>
                
                <li><t:message code="declarationText.line4" /></li>
                <li><t:message code="declarationText.line5" />
                    <a onmousedown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Report change online');" 
                       onkeydown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Report change online');" 
                       rel="external" target="_blank" href="https://www.gov.uk/carers-allowance-report-change"
                    ><t:message code="declarationText.line6" /></a>
                    <t:message code="declarationText.line1" />
                    <a onmousedown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Carer's Allowance Unit');" 
                       onkeydown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Carer's Allowance Unit');" 
                       rel="external" 
                       target="_blank" 
                       href="https://www.gov.uk/carers-allowance-unit"
                     ><t:message code="declarationText.line7" /></a>
                </li>
            </ul>
            <p class="warning-text">
                <t:message code="declarationText.line8" />
            </p>
        </t:htmlsection>

    </t:pageContent>

</t:mainPage>




