<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.personal-details" backLink="${previousPage}">
    
        <t:pre>
            <p>We look at any money your partner gets from benefits when we work out if you can get Carer's Allowance.</p>
        </t:pre>
        
        <t:yesnofield name="hadPartnerSinceClaimDate" />
        
        <t:hiddenPanel id="partnerDetailsWrap" triggerId="hadPartnerSinceClaimDate" triggerValue="yes">

            <t:textedit name="partnerDetailsTitle" maxLength="20" /> 
            <t:textedit name="partnerDetailsFirstName" maxLength="17" />
            <t:textedit name="partnerDetailsMiddleName" maxLength="17" />
            <t:textedit name="partnerDetailsSurname" maxLength="35" />
            <t:textedit name="partnerDetailsOtherNames" maxLength="35" />
            <t:textedit name="partnerDetailsNationalInsuranceNumber" maxLength="19" />  <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
            <t:datefield name="partnerDetailsDateOfBirth" />
            <t:textedit name="partnerDetailsNationality" maxLength="35" />
            <t:yesnofield name="partnerDetailsSeparated" />            
            <t:yesnofield name="isPartnerPersonYouCareFor" />
            
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
