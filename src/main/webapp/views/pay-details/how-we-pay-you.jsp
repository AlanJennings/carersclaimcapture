<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Your bank details" section="Section 10 of 11" backLink="${previousPage}">

        <t:htmlsection id="payment-explanation">
            <p>
                Carer's Allowance can be paid into a UK bank account, either your own or someone else's, 
                or your own Post Office card account.
                <br>
                You're responsible for repaying any overpayments, even if the money is paid 
                into someone else's account.
            </p>
        </t:htmlsection>
        
        <t:yesnofield id="likeToPay" 
                          name="likeToPay" 
                          value="${likeToPay}"
                          label="Do you have a bank account?" 
                          hintBefore="Don't include any information about your pension, if you get one."
                          errors="${validationErrors}" 
        />
        
        <t:hiddenWarning id="warningNoBankAccount" triggerId="likeToPay" triggerValue="no">
            <p>
                You won't be paid Carer's Allowance without a bank account. <br> 
                You can add bank details after you apply by contacting the Carer's Allowance Unit.
            </p>
        </t:hiddenWarning>
        
        <t:hiddenPanel id="empAdditionalInfoWrap" triggerId="likeToPay" triggerValue="yes">
            
            <t:textedit id="accountHolderName" name="accountHolderName" value="${accountHolderName}" maxLength="60" label="Account holder name" errors="${validationErrors}" />
            <t:textedit id="bankFullName" name="bankFullName" value="${bankFullName}" maxLength="60" label="Name of bank or building society" errors="${validationErrors}" />
            
            <t:sortcode id="sortcode" 
                        nameBlock1="sortcode_1" 
                        nameBlock2="sortcode_2" 
                        nameBlock3="sortcode_3" 
                        valueBlock1="${sortcode_1}" 
                        valueBlock2="${sortcode_2}" 
                        valueBlock3="${sortcode_3}" 
                        label="Sort code" 
                        errors="${validationErrors}" 
            />
            
            <t:textedit id="accountNumber" 
                        name="accountNumber" 
                        value="${accountNumber}" 
                        maxLength="10" 
                        label="Account number" 
                        hintAfter="If you're using a Post Office card account, your account number isn't the number on your card. 
                                    You can find the correct number on any letter you've had from the Post Office about your account."
                        errors="${validationErrors}" />
            
            <t:textedit id="rollOrReferenceNumber" name="rollOrReferenceNumber" value="${rollOrReferenceNumber}" maxLength="18" label="Building society roll or reference number (optional)" errors="${validationErrors}" />
            
        </t:hiddenPanel>

        <t:radiobuttons id="paymentFrequency" 
                        name="paymentFrequency" 
                        optionValues="Every week|Every four weeks|Every thirteen weeks"
                        optionLabels="Every week|Every four weeks|Every thirteen weeks"
                        value="${paymentFrequency}"
                        label="How often do you want to be paid Carer's Allowance?" 
                        errors="${validationErrors}" 
        />

    </t:pageContent>

</t:mainPage>    
