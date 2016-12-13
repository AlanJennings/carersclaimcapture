<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <t:yesnofield name="payPensionScheme" />

        <t:hiddenPanel id="payPensionSchemeTextWrap" triggerId="payPensionScheme" triggerValue="yes">
            <t:textarea name="payPensionSchemeText" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:yesnofield name="payForThings" />

        <t:hiddenPanel id="payForThingsTextWrap" triggerId="payForThings" triggerValue="yes">
            <t:textarea name="payForThingsText" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:yesnofield name="haveExpensesForJob" />

        <t:hiddenPanel id="haveExpensesForJobTextWrap" triggerId="haveExpensesForJob" triggerValue="yes">
            <t:textarea name="haveExpensesForJobText" showRemainingChars="true" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>