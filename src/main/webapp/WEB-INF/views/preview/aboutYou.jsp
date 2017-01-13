<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:accordion label="/about-you/your-details.pageTitle" openLabel="open-about-you" closeLabel="close-about-you" track="true">
    <t:fieldWithLink id="about_you_claimDate" name="dateOfClaim.label" value="cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, 'dd MMMMMMMMMM, yyyy', '')" disableLink="true" link="${dateOfClaimLink}" />
    <t:fieldWithLink id="about_you_marital_status" name="preview.maritalStatus.label" value="maritalStatus" disableLink="false" link="${carerMaritalStatusLink}" />
    <t:fieldWithLink id="about_you_full_name" name="preview.fullName.label" value="${carerFullName}" disableLink="false" link="${carerFullNameLink}" />
    <t:fieldWithLink id="about_you_dob" name="carerDateOfBirth.label" value="cads:dateOffset(carerDateOfBirth_day, carerDateOfBirth_month, carerDateOfBirth_year, 'dd MMMMMMMMMM, yyyy', '')" disableLink="false" link="${carerDateOfBirthLink}" />
    <t:fieldWithLink id="about_you_address" name="carerAddress.label" value="${carerAddressWithPostcode}" disableLink="false" link="carerAddressLink" />
    <t:fieldWithLink id="about_you_contact" name="carerHowWeContactYou.label" value="${carerHowWeContactYou}" disableLink="false" link="${carerHowWeContactYouLink}" />
    <t:fieldWithLink id="about_you_email" name="${emailLabel}" value="${emailValue}" disableLink="false" link="${carerEmailLink}" />
</t:accordion>

