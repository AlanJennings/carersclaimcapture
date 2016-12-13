<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:textedit name="careeTitle" />
        <t:textedit name="careeFirstName" />
        <t:textedit name="careeMiddleName" />
        <t:textedit name="careeSurname" />
        <t:textedit name="careeNationalInsuranceNumber" additionalClasses="ni-number" />   <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:datefield name="careeDateOfBirth" />
        <t:textedit name="careeRelationship" />
        <t:yesnofield name="careeSameAddress" />

        <t:hiddenPanel id="addressWrapper" triggerId="careeSameAddress" triggerValue="no">
            <t:address name="careeAddress" />
            <t:textedit name="careePostcode" additionalClasses="postcode" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
