<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.other-eea-state-or-switzerland" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.other-eea-state-or-switzerland" backLink="${previousPage}">
        
        <t:yesnofield name="eeaGuardQuestion" />
        
        <t:hiddenPanel id="eeaWrapper" triggerId="eeaGuardQuestion" triggerValue="yes">
            <t:pre>
                <p>
                    These questions are about the following countries:
                    <br>
                    Austria, Belgium, Bulgaria, Croatia, Cyprus, Czech Republic, 
                    Denmark, Estonia, Finland, France, Germany, Gibraltar, Greece, 
                    Hungary, Iceland, Ireland, Italy, Latvia, Liechtenstein, Lithuania, 
                    Luxembourg, Malta, Netherlands, Norway, Poland, Portugal, Romania, 
                    Slovakia, Slovenia, Spain, Sweden and Switzerland.
               </p>
           </t:pre>
           
           <t:yesnofield name="benefitsFromEEADetails" />
            
            <t:hiddenPanel id="benefitsFromEEADetailsWrapper" triggerId="benefitsFromEEADetails" triggerValue="yes">
                <t:textarea name="benefitsFromEEADetails_field" maxLength="3000" />
            </t:hiddenPanel>

            <t:yesnofield name="workingForEEADetails" />
            
            <t:hiddenPanel id="workingForEEADetailsWrapper" triggerId="workingForEEADetails" triggerValue="yes">
                <t:textarea name="workingForEEADetails_field" maxLength="3000" showRemainingChars="true" />
            </t:hiddenPanel>
            
        </t:hiddenPanel>
        
    </t:pageContent>

</t:mainPage>    
