<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="break-somewhere-else" backLink="${previousPage}">

        <t:htmlsection name="breakSomewhereElseIntro">
            <p><t:message code="breakSomewhereElseIntro.text"/></p>
        </t:htmlsection>

        <t:datefield name="careeSomewhereElseStartDate" />
        <t:textedit name="careeSomewhereElseStartTime" maxLength="10" outerStyle="display: none;" /><%-- visibility controlled by javascript --%>
        <t:yesnofield name="careeSomewhereElseEndedTime" />
        <t:datefield name="careeSomewhereElseEndDate" />
        <t:textedit name="careeSomewhereElseEndTime" maxLength="10" outerStyle="display: none;" /><%-- visibility controlled by javascript --%>

        <t:radiobuttons name="carerSomewhereElseWhereYou" optionValues="on holiday|at home|somewhere else" optionLabelKeys="holiday|home|elsewhere" />

        <t:hiddenPanel id="carerSomewhereElseWhereYouWrap" triggerId="carerSomewhereElseWhereYou" triggerValue="somewhere else">
            <t:textarea name="carerSomewhereElseWhereYouOtherText" maxLength="60" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:radiobuttons name="carerSomewhereElseWhereCaree" optionValues="on holiday|at home|somewhere else" optionLabelKeys="holiday|home|elsewhere"/>

        <t:hiddenPanel id="carerSomewhereElseWhereCareeWrap" triggerId="carerSomewhereElseWhereCaree" triggerValue="somewhere else">
            <t:textarea name="carerSomewhereElseWhereCareeOtherText" maxLength="60" showRemainingChars="true" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
