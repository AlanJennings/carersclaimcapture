<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="${employerName}" section="Section 9 of 11" backLink="${previousPage}">
        <t:yesnofield id="payPensionScheme" 
                      name="payPensionScheme" 
                      value="${payPensionScheme}"
                      label="Do you pay into a pension?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="payPensionSchemeTextWrap" triggerId="payPensionScheme" triggerValue="yes">
            <t:textarea id="payPensionSchemeText" 
                        name="payPensionSchemeText" 
                        value="${payPensionSchemeText}" 
                        maxLength="300" 
                        showRemainingChars="true"
                        label="Give details of each pension you pay into, including how much and how often you pay." 
                        errors="${validationErrors}"
           />
        </t:hiddenPanel>

        <t:yesnofield id="payForThings" 
                      name="payForThings" 
                      value="${payForThings}"
                      label="Do you pay for things you need to do your job?"
                      hintBefore="This means anything you have to pay for yourself - not expenses your company pays."
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="payForThingsTextWrap" triggerId="payForThings" triggerValue="yes">
            <t:textarea id="payForThingsText" 
                        name="payForThingsText" 
                        value="${payForThingsText}" 
                        maxLength="300" 
                        showRemainingChars="true"
                        label="Give details of what you need to buy, why you need it and how much it costs." 
                        errors="${validationErrors}"
           />
        </t:hiddenPanel>

        <t:yesnofield id="haveExpensesForJob" 
                      name="haveExpensesForJob" 
                      value="${haveExpensesForJob}"
                      label="Do you have any care costs because of this work?" 
                      hintBefore="This includes childcare costs as well as costs for looking after the person you care for."
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="haveExpensesForJobTextWrap" triggerId="haveExpensesForJob" triggerValue="yes">
            <t:textarea id="haveExpensesForJobText" 
                        name="haveExpensesForJobText" 
                        value="${haveExpensesForJobText}" 
                        maxLength="300" 
                        showRemainingChars="true"
                        label="Give details of who you pay and what it costs." 
                        hintBefore="If you pay a family member let us know their relationship to you and the person you care for."
                        errors="${validationErrors}"
           />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
