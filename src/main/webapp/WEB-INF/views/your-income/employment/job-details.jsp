<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:pre><p id="jobDetailsIntro"><t:message code="jobDetailsIntro.text" /></p></t:pre>

        <t:textedit name="employerName" />  
        <t:textedit name="employmentPhoneNumber" />
        <t:address name="employmentAddress" />
        <t:textedit name="employmentPostcode" additionalClasses="postcode" />
        <t:yesnofield name="startJobBeforeClaimDate" />

        <t:hiddenPanel id="startJobBeforeClaimDateWrap" triggerId="startJobBeforeClaimDate" triggerValue="no">
            <t:datefield name="jobStartDate" />
        </t:hiddenPanel>

        <t:yesnofield name="finishedThisJob" />

        <t:hiddenPanel id="finishedThisWrap" triggerId="finishedThisJob" triggerValue="yes">
            <t:datefield name="lastWorkDate" />
            <t:datefield name="p45LeavingDate" />
        </t:hiddenPanel>

        <t:textedit name="employmentHoursPerWeek" />

    </t:pageContent>

</t:mainPage>    
