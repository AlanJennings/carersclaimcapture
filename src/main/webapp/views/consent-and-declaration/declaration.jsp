<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Consent and declaration" section="" backLink="${previousPage}">
        
        <t:yesnofield id="tellUsWhyInformation" 
                      name="tellUsWhyInformation" 
                      value="${tellUsWhyInformation}"
                      label="Do you agree to the Carer's Allowance Unit contacting anyone mentioned in this form?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="tellUsWhyInformationWrap" triggerId="tellUsWhyInformation" triggerValue="no" >
            <t:textarea id="tellUsWhyPerson" 
                        name="tellUsWhyPerson" 
                        value="${tellUsWhyPerson}" 
                        maxLength="3000" 
                        showRemainingChars="true"
                        label="List anyone you don't want to be contacted and say why."
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <t:htmlsection>
            <p>By sending this application you agree that:</p>
            <ul class="list-bullet">
                <li>the information you've given is complete and correct as far as you know</li>
                
                    <li>the Carer's Allowance Unit may check your information with other government departments</li>
                
                <li>you'll pay back any money you've been overpaid if you're asked to</li>
                <li>you'll report changes of your circumstances or those of the person you care for promptly either
                    <a onmousedown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Report change online');" onkeydown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Report change online');" rel="external" target="_blank" href="https://www.gov.uk/carers-allowance-report-change">online</a>
                    or by contacting
                    <a onmousedown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Carer's Allowance Unit');" onkeydown="trackEvent('/consent-and-declaration/declaration','Claim Declaration - Carer's Allowance Unit');" rel="external" target="_blank" href="https://www.gov.uk/carers-allowance-unit">the Carer's Allowance Unit</a>
                </li>
            </ul>
            <p class="warning-text">
                You could be prosecuted or have to pay a financial penalty if you deliberately give wrong or incomplete information or don't report changes. Your Carer's Allowance may also be stopped or reduced.
            </p>
        </t:htmlsection>

    </t:pageContent>

</t:mainPage>




