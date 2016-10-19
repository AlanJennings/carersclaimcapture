<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="selfEmployedPayPensionScheme" />

        <t:hiddenPanel id="selfEmployedPayPensionSchemeTextWrap" triggerId="selfEmployedPayPensionScheme" triggerValue="yes" >
            <t:textarea name="selfEmployedPayPensionSchemeText" maxLength="300" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:yesnofield name="selfEmployedHaveExpensesForJob" />

        <t:hiddenPanel id="selfEmployedHaveExpensesForJobTextWrap" triggerId="selfEmployedHaveExpensesForJob" triggerValue="yes" >
            <t:textarea name="selfEmployedHaveExpensesForJobText" maxLength="300" showRemainingChars="true" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
