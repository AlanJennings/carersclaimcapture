<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">
    
    <t:pageContent errors="${validationErrors}" page="page.eligibility" backLink="${previousPage}">
    
        <t:yesnofield name="over35HoursAWeek" />
        
        <t:hiddenWarning id="warningover35HoursAWeek_answer" triggerId="over35HoursAWeek" triggerValue="no">
            <p>
                You can't get Carer's Allowance if you care for someone less than 35 hours a week. You might still be entitled to 
                <a rel="external" href="https://gov.uk/carers-credit" target="_blank"> Carer's Credit</a>.
            </p>
        </t:hiddenWarning>

        <t:yesnofield name="over16YearsOld" />
        
        <t:hiddenWarning id="warningover16_answer" triggerId="over16YearsOld" triggerValue="no">
            <p>You can only get Carer's Allowance if you're 16 or over.</p>
        </t:hiddenWarning>

        <t:radiobuttons name="originCountry" optionValues="GB|NI|OTHER" />
        
        <t:hiddenWarning id="warningOriginCountry_answer" triggerId="originCountry" triggerValue="OTHER">
            <p>
                You can normally get Carer's Allowance if you live in England, Scotland or Wales. 
                You might get it if you live in the EEA (European Economic Area) or Switzerland, but you 
                must apply to find out.
            </p>
        </t:hiddenWarning>
        
    </t:pageContent>
    
</t:mainPage>