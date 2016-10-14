<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 
<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="beenSelfEmployedSince1WeekBeforeClaim"/>
        <t:yesnofield name="beenEmployedSince6MonthsBeforeClaim" />
        <t:yesnofield name="hadOtherIncomeSinceClaimDate" />
<%--       
        <spring:message code="ibeenInEducationSinceClaimDate.hintBefore.args" />
        <spring:message code="ibeenInEducationSinceClaimDate.hintBefore.args" var="args"/>
        <cads:resolveArgs var="args">${pageScope.args}</cads:resolveArgs>
 --%>         
        <t:hiddenPanel id="otherIncomeWrap" triggerId="hadOtherIncomeSinceClaimDate" triggerValue="yes">
            <t:group name="otherIncomeGroup">
                <t:checkbox name="yourIncome_sickpay" outerClass="no-class" />
                <t:checkbox name="yourIncome_patmatadoppay" outerClass="no-class" />
                <t:checkbox name="yourIncome_fostering" outerClass="no-class" />
                <t:checkbox name="yourIncome_directpay" outerClass="no-class" />
                <t:checkbox name="yourIncome_anyother" outerClass="no-class" />
           </t:group>
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
