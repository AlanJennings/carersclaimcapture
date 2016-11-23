<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="directPaymentStillBeingPaidThisPay" />

        <t:hiddenPanel id="directPaymentStillBeingPaidThisPayWrap" triggerId="directPaymentStillBeingPaidThisPay" triggerValue="no">
            <t:datefield name="directPaymentWhenDidYouLastGetPaid" />
        </t:hiddenPanel>

        <t:textedit name="directPaymentWhoPaidYouThisPay" maxLength="60" /> 
        <t:textedit name="directPaymentAmountOfThisPay" maxLength="12" />
        <t:radiobuttons name="directPaymentHowOftenPaidThisPay" optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other" />

        <t:hiddenPanel id="directPaymentHowOftenPaidThisPayWrap" triggerId="directPaymentHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit name="directPaymentHowOftenPaidThisPayOther" maxLength="60" /> 
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
