<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        
        <t:yesnofield name="stillSelfEmployed" />
        
        <t:hiddenPanel id="stillSelfEmployedWrap" triggerId="stillSelfEmployed" triggerValue="no" >
            <t:datefield name="finishThisWork" />
        </t:hiddenPanel>

        <t:yesnofield name="moreThanYearAgo" />
        
        <t:hiddenPanel id="moreThanYearAgoWrapYes" triggerId="moreThanYearAgo" triggerValue="yes" >
            <t:yesnofield name="haveAccounts" />
            
            <t:hiddenWarning id="haveAccountsWrapYes" triggerId="haveAccounts" triggerValue="yes" >
                <t:message code="haveAccountsWrapYes.text" />
            </t:hiddenWarning>
            
            <t:hiddenPanel id="haveAccountsWrapNo" triggerId="haveAccounts" triggerValue="no" >
                <t:yesnofield name="knowTradingYear" />

                <t:hiddenPanel id="knowTradingYearWrap" triggerId="knowTradingYear" triggerValue="yes" >
                    <t:datefield name="tradingYearStart" />
                </t:hiddenPanel>
            </t:hiddenPanel>
        </t:hiddenPanel>
        
        <t:hiddenPanel id="moreThanYearAgoWrapNo" triggerId="moreThanYearAgo" triggerValue="no" >
            
            <t:datefield name="startThisWork" />
            <t:yesnofield name="paidMoney" />
             
            <t:hiddenPanel id="paidMoneyWrap" triggerId="paidMoney" triggerValue="yes" >
                <t:datefield name="paidMoneyDate" />
            </t:hiddenPanel>
                    
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
