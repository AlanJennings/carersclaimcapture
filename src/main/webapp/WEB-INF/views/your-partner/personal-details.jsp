<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:pre>
            <p><t:message code="partnerDetails.intro" /></p>
        </t:pre>

        <t:group name="partnerDetailsInstructions">
        
            <t:yesnofield name="hadPartnerSinceClaimDate" />
            
            <t:hiddenPanel id="partnerDetailsWrap" triggerId="hadPartnerSinceClaimDate" triggerValue="yes">

                <t:textedit name="partnerDetailsTitle" maxLength="20" /> 
                <t:textedit name="partnerDetailsFirstName" maxLength="17" />
                <t:textedit name="partnerDetailsMiddleName" maxLength="17" />
                <t:textedit name="partnerDetailsSurname" maxLength="35" />
                <t:textedit name="partnerDetailsOtherNames" maxLength="35" />
                <t:textedit name="partnerDetailsNationalInsuranceNumber" maxLength="19" additionalClasses="ni-number" />  <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
                <t:datefield name="partnerDetailsDateOfBirth" />
                <t:textedit name="partnerDetailsNationality" maxLength="35" />
                <t:yesnofield name="partnerDetailsSeparated" />            
                <t:yesnofield name="isPartnerPersonYouCareFor" />

            </t:hiddenPanel>
        </t:group>

    </t:pageContent>

</t:mainPage>    
