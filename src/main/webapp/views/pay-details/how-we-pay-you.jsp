<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.how-we-pay-you" backLink="${previousPage}">

        <t:htmlsection name="payment-explanation">
            <p>
                <t:message code="payment-explanation.line1" />
                <br>
                <t:message code="payment-explanation.line2" />
           </p>
        </t:htmlsection>

        <t:yesnofield name="likeToPay" />

        <t:hiddenWarning id="warningNoBankAccount" triggerId="likeToPay" triggerValue="no">
            <p>
                <t:message code="warningNoBankAccount.line1" />
                <br>
                <t:message code="warningNoBankAccount.line2" />
            </p>
        </t:hiddenWarning>

        <t:hiddenPanel id="empAdditionalInfoWrap" triggerId="likeToPay" triggerValue="yes">

            <t:textedit name="accountHolderName" maxLength="60" />
            <t:textedit name="bankFullName" maxLength="60" />
            <t:sortcode name="sortcode" />
            <t:textedit name="accountNumber" maxLength="10" />
            <t:textedit name="rollOrReferenceNumber" maxLength="18" />

        </t:hiddenPanel>

        <t:radiobuttons name="paymentFrequency" optionValues="Every week|Every four weeks|Every thirteen weeks" optionLabelKeys="Weekly|Four-weekly|Quarterly" />

    </t:pageContent>

</t:mainPage> 
