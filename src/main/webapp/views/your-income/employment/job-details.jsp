<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.job-details" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.job-details" backLink="${previousPage}">

        <t:pre><p id="jobDetailsIntro"><t:message code="jobDetailsIntro.text" /></p></t:pre>

        <t:textedit name="employerName" maxLength="60" />
        <t:textedit name="employmentPhoneNumber" maxLength="20" />
        <t:address name="employerAddress" maxlength="35" />
        <t:textedit name="employmentPostcode" maxLength="10" />
        <t:yesnofield name="startJobBeforeClaimDate" />

        <t:hiddenPanel id="startJobBeforeClaimDateWrap" triggerId="startJobBeforeClaimDate" triggerValue="no">
            <t:datefield name="jobStartDate" />
        </t:hiddenPanel>

        <t:yesnofield name="finishedThisJob" />

        <t:hiddenPanel id="finishedThisWrap" triggerId="finishedThisJob" triggerValue="yes">
            <t:datefield name="lastWorkDate" />
            <t:datefield name="p45LeavingDate" />
        </t:hiddenPanel>

        <t:textedit name="employmentHoursPerWeek" maxLength="5" />

    </t:pageContent>

</t:mainPage>    
