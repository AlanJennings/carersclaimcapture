<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:htmlsection name="aboutYou.yourDetails">
            <p><t:message code="aboutYou.text" /></p>
        </t:htmlsection>

        <t:textedit name="firstName" />
        <t:textedit name="lastName" />
        <t:textedit name="nationalInsuranceNumber" additionalClasses="ni-number" />
        <t:datefield name="dateOfBirth" />

        <t:htmlsection name="contactDetails" />

        <t:textedit name="contactNumber" />
        <t:yesnofield name="emailConfirmationWanted" />
        <t:hiddenPanel id="emailAddressWrapper" triggerId="emailConfirmationWanted" triggerValue="yes">
            <t:textedit name="emailAddress" />
            <t:textedit name="emailAddressConfirm" />
        </t:hiddenPanel>

        <t:htmlsection name="personYouCareFor" />

        <t:textedit name="careeFirstName" />
        <t:textedit name="careeLastName" />
        <t:textedit name="careeRelationship" />

    </t:pageContent>
    <t:googleAnalyticsTiming tracktype="STARTELIGIBLE"/>
</t:mainPage>
