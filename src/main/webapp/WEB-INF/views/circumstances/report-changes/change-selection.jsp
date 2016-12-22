<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}">
        <t:radiobuttons name="changeTypeAnswer" optionValues="stoppedProvidingCare|incomeChanged|patientAway|carerAway|changeOfAddress|changePaymentDetails|somethingElse" />
        <t:hiddenWarning id="answerNoMessageWrap" triggerId="changeTypeAnswer" triggerValue="NONE">
            <p><t:message code="changeType.warning.message" /></p>
        </t:hiddenWarning>
    </t:pageContent>
    <t:googleAnalyticsTiming tracktype="STARTELIGIBLE"/>
</t:mainPage>