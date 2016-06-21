<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Partner details - About your partner" section="Section 5 of 11" backLink="${previousPage}">
        
        <t:textedit id="title" name="title" value="${title}" maxLength="20" label="Title" errors="${validationErrors}" /> 
        <t:textedit id="firstName" name="firstName" value="${firstName}" maxLength="17" label="First name" errors="${validationErrors}" />
        <t:textedit id="middleName" name="middleName" value="${middleName}" maxLength="17" label="Middle name(s)" errors="${validationErrors}" />
        <t:textedit id="surname" name="surname" value="${surname}" maxLength="35" label="Last name" errors="${validationErrors}" />
        
        <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:textedit id="nationalInsuranceNumber" 
                    name="nationalInsuranceNumber" 
                    value="${nationalInsuranceNumber}" 
                    maxLength="19" 
                    label="National Insurance number" 
                    errors="${validationErrors}" 
                    additionalClasses="ni-number"
                    hintBefore="For example, VO123456D"
                    hintAfter="This is on your NI number card, benefit letter, payslip or P60."
        />
        
        <t:datefield id="dateOfBirth" 
                     nameDay="dateOfBirth_day" 
                     nameMonth="dateOfBirth_month" 
                     nameYear="dateOfBirth_year" 
                     valueDay="${dateOfBirth_day}" 
                     valueMonth="${dateOfBirth_month}" 
                     valueYear="${dateOfBirth_year}" 
                     label="Date of birth" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 16 5 1976" 
                     hintBeforeId="dateOfBirth_defaultDateContextualHelp"
        />
        
        <t:textedit id="relationship" 
                    name="relationship" 
                    value="${relationship}" 
                    maxLength="35" 
                    label="What's their relationship to you?" 
                    errors="${validationErrors}" 
                    hintBefore="For example, father, mother, son, daughter."
        />
        
        <t:yesnofield id="sameAddress" name="sameAddress" value="${sameAddress}" label="Do they live at the same address as you?" errors="${validationErrors}" />
        
        <t:hiddenPanel id="addressWrapper" triggerId="sameAddress" triggerValue="no">
            <t:address id="address" 
                       nameOne="address_lineOne"
                       nameTwo="address_lineTwo"
                       nameThree="address_lineThree"
                       valueOne="${address_lineOne}"
                       valueTwo="${address_lineTwo}"
                       valueThree="${address_lineThree}"
                       maxlength="35"
                       label="Address"
                       errors="${validationErrors}"
            />
            
            <t:textedit id="postcode" name="postcode" value="${postcode}" maxLength="10" label="Postcode" errors="${validationErrors}" />
            
        </t:hiddenPanel>


    </t:pageContent>
    
    

</t:mainPage>    
