<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <script type="text/javascript" src="<c:url value='/javascript/isMondayOrFriday.js' />" ></script>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <input type="hidden" name="breakInCareOther" value="elsewhere" >
        
        <t:htmlsection name="breakSomewhereElseIntro">
            <p><t:message code="breakSomewhereElseIntro.text"/></p>
        </t:htmlsection>

        <t:datefield name="careeSomewhereElseStartDate" />
        
        <t:hiddenPanel id="careeSomewhereElseStartTimeWrapper" triggerId="careeSomewhereElseStartDate" triggerFunction="isMondayOrFriday">
            <t:textedit name="careeSomewhereElseStartTime" />
        </t:hiddenPanel>
        
        <t:yesnofield name="careeSomewhereElseEndedTime" />
        
        <t:hiddenPanel id="careeSomewhereElseEndedTimeWrap" triggerId="careeSomewhereElseEndedTime" triggerValue="yes">
            <t:datefield name="careeSomewhereElseEndDate" />
            <t:hiddenPanel id="careeSomewhereElseEndTimeWrapper" triggerId="careeSomewhereElseEndDate" triggerFunction="isMondayOrFriday">
                <t:textedit name="careeSomewhereElseEndTime" />
            </t:hiddenPanel>
        </t:hiddenPanel>
        
        <t:radiobuttons name="carerSomewhereElseWhereYou" optionValues="on holiday|at home|somewhere else" optionLabelKeys="holiday|home|elsewhere" />

        <t:hiddenPanel id="carerSomewhereElseWhereYouWrap" triggerId="carerSomewhereElseWhereYou" triggerValue="somewhere else">
            <t:textarea name="carerSomewhereElseWhereYouOtherText" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:radiobuttons name="carerSomewhereElseWhereCaree" optionValues="on holiday|at home|somewhere else" optionLabelKeys="holiday|home|elsewhere"/>

        <t:hiddenPanel id="carerSomewhereElseWhereCareeWrap" triggerId="carerSomewhereElseWhereCaree" triggerValue="somewhere else">
            <t:textarea name="carerSomewhereElseWhereCareeOtherText" showRemainingChars="true" />
        </t:hiddenPanel>
    </t:pageContent>
</t:mainPage>   
