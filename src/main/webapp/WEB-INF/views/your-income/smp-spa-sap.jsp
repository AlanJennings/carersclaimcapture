<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:radiobuttons name="otherStatPaymentPaymentTypesForThisPay" optionValues="MaternityOrPaternityPay|AdoptionPay" />
        <t:yesnofield name="otherStatPaymentStillBeingPaidThisPay" />

        <t:hiddenPanel id="otherStatPaymentStillBeingPaidThisPayWrap" triggerId="otherStatPaymentStillBeingPaidThisPay" triggerValue="no">
            <t:datefield name="otherStatPaymentWhenDidYouLastGetPaid" />
        </t:hiddenPanel>

        <t:textedit name="otherStatPaymentWhoPaidYouThisPay" maxLength="60" /> 
        <t:textedit name="otherStatPaymentAmountOfThisPay" maxLength="12" outerClass="form-group short-field" />
        <t:radiobuttons name="otherStatPaymentHowOftenPaidThisPay" optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other" />

        <t:hiddenPanel id="otherStatPaymentHowOftenPaidThisPayWrap" triggerId="otherStatPaymentHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit name="otherStatPaymentHowOftenPaidThisPayOther" maxLength="60" /> 
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
