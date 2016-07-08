<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Self-employment" section="Section 9 of 11" backLink="${previousPage}">
        
        <t:yesnofield id="stillSelfEmployed" 
                      name="stillSelfEmployed" 
                      value="${stillSelfEmployed}"
                      label="Are you still doing this work?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="stillSelfEmployedWrap" triggerId="stillSelfEmployed" triggerValue="no" >
            <t:datefield id="finishThisWork" 
                         nameDay="finishThisWork_day" 
                         nameMonth="finishThisWork_month" 
                         nameYear="finishThisWork_year" 
                         valueDay="${finishThisWork_day}" 
                         valueMonth="${finishThisWork_month}" 
                         valueYear="${finishThisWork_year}" 
                         label="When did you finish this work?" 
                         errors="${validationErrors}" 
            />
        </t:hiddenPanel>

        <t:yesnofield id="moreThanYearAgo" 
                      name="moreThanYearAgo" 
                      value="${moreThanYearAgo}"
                      label="Did you start this work more than a year ago?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="moreThanYearAgoWrapYes" triggerId="moreThanYearAgo" triggerValue="yes" >
            <t:yesnofield id="haveAccounts" 
                      name="haveAccounts" 
                      value="${haveAccounts}"
                      label="Do you have accounts?" 
                      errors="${validationErrors}" 
            />
            
            <t:hiddenWarning id="haveAccountsWrapYes" triggerId="haveAccounts" triggerValue="yes" >
                <p>You must send a copy of your most recent accounts to the Carer's Allowance Unit.</p>
            </t:hiddenWarning>
            
            <t:hiddenPanel id="haveAccountsWrapNo" triggerId="haveAccounts" triggerValue="no" >
                <t:yesnofield id="knowTradingYear" 
                      name="knowTradingYear" 
                      value="${knowTradingYear}"
                      label="Do you know your trading year?" 
                      hintBefore="This is the date you do your accounts to each year - it's often the same as the tax year, but it doesn't have to be."
                      errors="${validationErrors}" 
                />
                
                <t:hiddenPanel id="knowTradingYearWrap" triggerId="knowTradingYear" triggerValue="yes" >
                    
                    <t:datefield id="tradingYearStart" 
                                 nameDay="tradingYearStart_day" 
                                 nameMonth="tradingYearStart_month" 
                                 nameYear="tradingYearStart_year" 
                                 valueDay="${tradingYearStart_day}" 
                                 valueMonth="${tradingYearStart_month}" 
                                 valueYear="${tradingYearStart_year}" 
                                 label="Trading year start date" 
                                 errors="${validationErrors}" 
                    />
                </t:hiddenPanel>

            </t:hiddenPanel>
        </t:hiddenPanel>
        
        <t:hiddenPanel id="moreThanYearAgoWrapNo" triggerId="moreThanYearAgo" triggerValue="no" >
            
            <t:datefield id="startThisWork" 
                             nameDay="startThisWork_day" 
                             nameMonth="startThisWork_month" 
                             nameYear="startThisWork_year" 
                             valueDay="${startThisWork_day}" 
                             valueMonth="${startThisWork_month}" 
                             valueYear="${startThisWork_year}" 
                             label="When did you start this work?" 
                             errors="${validationErrors}" 
            />
            
            <t:yesnofield id="paidMoney" 
                          name="paidMoney" 
                          value="${paidMoney}"
                          label="Has your self-employed business been paid any money yet?" 
                          hintBefore="This includes money paid directly to you or into a business account."
                          errors="${validationErrors}" 
             />
             
             <t:hiddenPanel id="paidMoneyWrap" triggerId="paidMoney" triggerValue="yes" >
             
                <t:datefield id="paidMoneyDate" 
                             nameDay="paidMoneyDate_day" 
                             nameMonth="paidMoneyDate_month" 
                             nameYear="paidMoneyDate_year" 
                             valueDay="${paidMoneyDate_day}" 
                             valueMonth="${paidMoneyDate_month}" 
                             valueYear="${paidMoneyDate_year}" 
                             label="Date money first received by the business" 
                             errors="${validationErrors}" 
                />
             
             </t:hiddenPanel>
                    
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
