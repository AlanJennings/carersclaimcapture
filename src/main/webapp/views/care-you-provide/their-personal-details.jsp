<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.their-personal-details" backLink="${previousPage}">

        <t:textedit name="careeTitle" maxLength="20" /> 
        <t:textedit name="careeFirstName" maxLength="17" />
        <t:textedit name="careeMiddleName" maxLength="17" />
        <t:textedit name="careeSurname" maxLength="35" />
        <t:textedit name="careeNationalInsuranceNumber" maxLength="19" />  <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:datefield name="careeDateOfBirth" />
        <t:textedit name="careeRelationship" maxLength="35" />
        <t:yesnofield name="careeSameAddress" />

        <t:hiddenPanel id="addressWrapper" triggerId="sameAddress" triggerValue="no">
            <t:address name="careeAddress" maxlength="35" />
            <t:textedit name="careePostcode" maxLength="10" />
        </t:hiddenPanel>

    </t:pageContent>
    
</t:mainPage>    
