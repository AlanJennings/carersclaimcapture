<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="hadPartnerSinceClaimDate" />

        <t:hiddenPanel id="partnerDetailsWrap" triggerId="hadPartnerSinceClaimDate" triggerValue="yes">

            <t:textedit name="partnerDetailsTitle" />      
            <t:textedit name="partnerDetailsFirstName" />     
            <t:textedit name="partnerDetailsMiddleName" />     
            <t:textedit name="partnerDetailsSurname" />     
            <t:textedit name="partnerDetailsOtherNames" />     
            <t:textedit name="partnerDetailsNationalInsuranceNumber" additionalClasses="ni-number" /> <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
            <t:datefield name="partnerDetailsDateOfBirth" />
            <t:textedit name="partnerDetailsNationality" />     
            <t:yesnofield name="partnerDetailsSeparated" />            
            <t:yesnofield name="isPartnerPersonYouCareFor" />

        </t:hiddenPanel>


    </t:pageContent>

</t:mainPage>    
