<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Self-employment" section="Section 9 of 11" backLink="${previousPage}">

        <t:yesnofield id="payPensionScheme" 
                      name="payPensionScheme" 
                      value="${payPensionScheme}"
                      label="Did you pay into a pension?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="payPensionSchemeTextWrap" triggerId="payPensionScheme" triggerValue="yes" >
            <t:textarea id="payPensionSchemeText" 
                        name="payPensionSchemeText" 
                        value="${payPensionSchemeText}" 
                        maxLength="300" 
                        showRemainingChars="true"
                        label="Give details of each pension you paid into, including how much and how often you paid into them."
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <t:yesnofield id="haveExpensesForJob" 
                      name="haveExpensesForJob" 
                      value="${haveExpensesForJob}"
                      label="Did you have any care costs because of this work?" 
                      hintBefore="This includes childcare costs as well as costs for looking after the person you care for."
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="haveExpensesForJobTextWrap" triggerId="haveExpensesForJob" triggerValue="yes" >
            <t:textarea id="haveExpensesForJobText" 
                        name="haveExpensesForJobText" 
                        value="${haveExpensesForJobText}" 
                        maxLength="300" 
                        showRemainingChars="true"
                        label="Give details of who you paid and what it cost."
                        hintBefore="If you paid a family member let us know their relationship to you and the person you care for."
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
