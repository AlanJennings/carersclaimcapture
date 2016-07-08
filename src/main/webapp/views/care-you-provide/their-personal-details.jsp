<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Partner details - About your partner" section="Section 5 of 11" backLink="${previousPage}">
                                                    
        <t:textedit id="careeTitle" name="careeTitle" value="${careeTitle}" maxLength="20" label="Title" errors="${validationErrors}" /> 
        <t:textedit id="careeFirstName" name="careeFirstName" value="${careeFirstName}" maxLength="17" label="First name" errors="${validationErrors}" />
        <t:textedit id="careeMiddleName" name="careeMiddleName" value="${careeMiddleName}" maxLength="17" label="Middle name(s)" errors="${validationErrors}" />
        <t:textedit id="careeSurname" name="careeSurname" value="${careeSurname}" maxLength="35" label="Last name" errors="${validationErrors}" />
        
        <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:textedit id="careeNationalInsuranceNumber" 
                    name="careeNationalInsuranceNumber" 
                    value="${careeNationalInsuranceNumber}" 
                    maxLength="19" 
                    label="National Insurance number" 
                    errors="${validationErrors}" 
                    additionalClasses="ni-number"
                    hintBefore="For example, VO123456D"
                    hintAfter="This is on your NI number card, benefit letter, payslip or P60."
        />
        
        <t:datefield id="careeDateOfBirth" 
                     nameDay="careeDateOfBirth_day" 
                     nameMonth="careeDateOfBirth_month" 
                     nameYear="careeDateOfBirth_year" 
                     valueDay="${careeDateOfBirth_day}" 
                     valueMonth="${careeDateOfBirth_month}" 
                     valueYear="${careeDateOfBirth_year}" 
                     label="Date of birth" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 16 5 1976" 
                     hintBeforeId="dateOfBirth_defaultDateContextualHelp"
        />
        
        <t:textedit id="careeRelationship" 
                    name="careeRelationship" 
                    value="${careeRelationship}" 
                    maxLength="35" 
                    label="What's their relationship to you?" 
                    errors="${validationErrors}" 
                    hintBefore="For example, father, mother, son, daughter."
        />
        
        <t:yesnofield id="careeSameAddress" name="careeSameAddress" value="${sameAddress}" label="Do they live at the same address as you?" errors="${validationErrors}" />
        
        <t:hiddenPanel id="addressWrapper" triggerId="sameAddress" triggerValue="no">
            <t:address id="careeAddress" 
                       nameOne="careeAddressLineOne"
                       nameTwo="careeAddressLineTwo"
                       nameThree="careeAddressLineThree"
                       valueOne="${careeAddressLineOne}"
                       valueTwo="${careeAddressLineTwo}"
                       valueThree="${careeAddressLineThree}"
                       maxlength="35"
                       label="Address"
                       errors="${validationErrors}"
            />
            
            <t:textedit id="careePostcode" name="careePostcode" value="${careePostcode}" maxLength="10" label="Postcode" errors="${validationErrors}" />
            
        </t:hiddenPanel>

    </t:pageContent>
    
</t:mainPage>    
