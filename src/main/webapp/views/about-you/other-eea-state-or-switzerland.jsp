<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Payments from abroad - Nationality and where you've lived" section="Section 4 of 11" backLink="${previousPage}">
        
        <t:yesnofield id="eeaGuardQuestion" 
                      name="eeaGuardQuestion" 
                      value="${eeaGuardQuestion}"
                      label="Have you or any of your close family worked abroad or been paid benefits from outside the United Kingdom since your claim date?" 
                      hintBefore="<p class="form-hint">This means your partner, parents or children and includes state pensions.</p>"
                      errors="${validationErrors}" 
        />
        
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
           
           <t:yesnofield id="benefitsFromEEADetails" 
                      name="benefitsFromEEADetails" 
                      value="${benefitsFromEEADetails}"
                      label="Have you or your close family claimed or been paid any benefits or pensions from any of these countries since your claim date?" 
                      hintBefore="<p class="form-hint">This means your partner, parents or children.</p>"
                      errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="benefitsFromEEADetailsWrapper" triggerId="benefitsFromEEADetails" triggerValue="yes">
                <t:textarea id="benefitsFromEEADetails_field"
                        name="benefitsFromEEADetails_field"
                        value="${benefitsFromEEADetails_field}"
                        maxLength="3000"
                        showRemainingChars="true"
                        label="Give details" 
                        hintBefore='<p class="form-hint">Include who was paid, the local name of the benefit, the amount and the dates of any payments.</p>'
                        errors="${validationErrors}"
                />
            </t:hiddenPanel>
            
            <t:yesnofield id="workingForEEADetails" 
                      name="workingForEEADetails" 
                      value="${workingForEEADetails}"
                      label="Have you or your close family worked or paid national insurance in any of these countries since your claim date?" 
                      hintBefore="<p class="form-hint">This means your partner, parents or children.</p>"
                      errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="workingForEEADetailsWrapper" triggerId="workingForEEADetails" triggerValue="yes">
                <t:textarea id="workingForEEADetails_field"
                            name="workingForEEADetails_field"
                            value="${workingForEEADetails_field}"
                            maxLength="3000"
                            showRemainingChars="true"
                            label="Give details" 
                            hintBefore='<p class="form-hint">Include who this applies to, when this happened and which country they worked or paid national insurance in.</p>'
                            errors="${validationErrors}"
                />
            </t:hiddenPanel>
            
        </t:hiddenPanel>
        
    </t:pageContent>
    

</t:mainPage>    
