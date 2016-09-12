<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.statutory-sick-pay" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.statutory-sick-pay" backLink="${previousPage}">

        <t:yesnofield name="stillBeingPaidThisPay" />
        
        <t:hiddenPanel id="stillBeingPaidThisPayWrap" triggerId="stillBeingPaidThisPay" triggerValue="no">
            <t:datefield name="whenDidYouLastGetPaid" />
        </t:hiddenPanel>

        <t:textedit name="whoPaidYouThisPay" maxLength="60" />
        <t:textedit name="amountOfThisPay" maxLength="12" />
        <t:radiobuttons name="howOftenPaidThisPay" optionValues="Weekly|Fortnightly|Four-Weekly|Monthly|Other" />

        <t:hiddenPanel id="howOftenPaidThisPayWrap" triggerId="howOftenPaidThisPay" triggerValue="Other">
            <t:textedit name="howOftenPaidThisPayOther" maxLength="60" />
        </t:hiddenPanel>
        
    </t:pageContent>

</t:mainPage>    




