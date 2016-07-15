<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.last-wage" pageTitle="${employerName}" backLink="${previousPage}">

        <t:select name="employmentPaymentFrequency" optionValues="|Single|Weekly|Fortnightly|Four-weekly|Monthly|Other" />

        <t:hiddenPanel id="employmentPaymentFrequencyOtherWrap" triggerId="employmentPaymentFrequency" triggerValue="Other">
            <t:textarea name="employmentPaymentFrequencyOtherText" maxLength="60" showRemainingChars="true" additionalClasses="textarea-reduced" />
        </t:hiddenPanel>

        <t:textedit name="employmentwhenGetPaid" maxLength="60" />
        <t:datefield name="employmentLastPaidDate" />
        <t:textedit name="employmentGrossPay" maxLength="12" />
        <t:textarea name="employmentPayInclusions" maxLength="300" showRemainingChars="true" />
        <t:yesnofield name="employmentSameAmountEachTime" />

    </t:pageContent>

</t:mainPage>

