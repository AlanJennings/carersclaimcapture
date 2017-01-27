<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 
<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="beenEmployedSince6MonthsBeforeClaim" />
        <t:yesnofield name="beenSelfEmployedSince1WeekBeforeClaim"/>
        <t:group name="otherIncomeGroup">
            <t:checkbox name="yourIncome_sickpay" outerClass="no-class" />
            <t:checkbox name="yourIncome_patmatadoppay" outerClass="no-class" />
            <t:checkbox name="yourIncome_fostering" outerClass="no-class" />
            <t:checkbox name="yourIncome_directpay" outerClass="no-class" />
            <t:checkbox name="yourIncome_rentalincome" outerClass="no-class" />
            <t:checkbox name="yourIncome_anyother" outerClass="no-class" />
            <t:checkbox name="yourIncome_none" outerClass="no-class" checkedValue="none" />
       </t:group>
    </t:pageContent>

</t:mainPage>    
