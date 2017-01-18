<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:accordion label="/care-you-provide/their-personal-details.pageTitle" openLabel="open-care-you-provide" closeLabel="close-care-you-provide" track="true">
	<c:if test="${showHidePartnerDetails}" >
        <t:fieldWithLink id="care_you_provide_name" name="preview.fullName.label" value="${careeFullName}" displayChangeButton="${displayChangeButton}" link="${careeFullNameLink}" />
        <t:fieldWithLink id="care_you_provide_dob" name="careeDateOfBirth.label" value="${cads:dateOffset(careeDateOfBirth_day, careeDateOfBirth_month, careeDateOfBirth_year, 'dd MMMMMMMMMM, yyyy', '')}" displayChangeButton="${displayChangeButton}" link="${careeDateOfBirthLink}" />
    </c:if>
    <t:fieldWithLink id="care_you_provide_relationship" name="careeRelationship.label" value="${careeRelationship}" displayChangeButton="${displayChangeButton}" link="${careeRelationshipLink}" />
    <t:fieldWithLink id="care_you_provide_address" name="careeSameAddress.label" value="${careeSameAddressWithPostcode}" displayChangeButton="${displayChangeButton}" link="${careeAddressLink}" />
    <t:fieldWithLink id="care_you_provide_spent35HoursCaring" name="spent35HoursCaring.label" value="${spent35HoursCaring}" displayChangeButton="${displayChangeButton}" link="${spent35HoursCaringLink}" />
    <c:if test="${isOriginGB}" >
        <t:fieldWithLink id="care_you_provide_otherCarer" name="otherCarer.label" value="${otherCarer}" displayChangeButton="${displayChangeButton}" link="${otherCarerLink}" />
        <c:if test="${showOtherCarer}" >
            <t:fieldWithLink id="care_you_provide_otherCarerUc" name="otherCarerUc.label" value="${otherCarerUc}" displayChangeButton="${displayChangeButton}" link="${otherCarerUcLink}" />
        </c:if>
        <c:if test="${showOtherCarerUc}" >
            <t:fieldWithLink id="care_you_provide_otherCarerUcDetails" name="otherCarerUcDetails.label" value="${otherCarerUcDetails}" displayChangeButton="${displayChangeButton}" link="${otherCarerUcDetailsLink}" />
        </c:if>
    </c:if>
</t:accordion>