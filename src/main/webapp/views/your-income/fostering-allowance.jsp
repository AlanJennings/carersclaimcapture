<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.fostering-allowance" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.fostering-allowance" backLink="${previousPage}">

        <t:radiobuttons name="fosteringAllowancePay" optionValues="LocalAuthority|FosteringAllowance|Other" />

        <t:hiddenPanel id="fosteringAllowancePayWrap" triggerId="fosteringAllowancePay" triggerValue="Other">
            <t:textedit name="fosteringAllowancePayOther" maxLength="60" /> 
        </t:hiddenPanel>

        <t:yesnofield name="fosteringAllowanceStillBeingPaidThisPay" />

        <t:hiddenPanel id="fosteringAllowanceStillBeingPaidThisPayWrap" triggerId="fosteringAllowanceStillBeingPaidThisPay" triggerValue="no">
            <t:datefield name="fosteringAllowanceWhenDidYouLastGetPaid" />
        </t:hiddenPanel>

        <t:textedit name="fosteringAllowanceWhoPaidYouThisPay" maxLength="60" /> 
        <t:textedit name="fosteringAllowanceAmountOfThisPay" maxLength="12" outerClass="short-field" />
        <t:radiobuttons name="fosteringAllowanceHowOftenPaidThisPay" optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other" />

        <t:hiddenPanel id="fosteringAllowanceHowOftenPaidThisPayWrap" triggerId="fosteringAllowanceHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit name="fosteringAllowanceHowOftenPaidThisPayOther" maxLength="60" /> 
        </t:hiddenPanel>

    </t:pageContent>
</t:mainPage>
