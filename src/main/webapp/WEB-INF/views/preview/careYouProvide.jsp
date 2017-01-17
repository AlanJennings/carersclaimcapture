<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:accordion label="/care-you-provide/their-personal-details.pageTitle" openLabel="open-care-you-provide" closeLabel="close-care-you-provide" track="true">
	<c:if test="${showHidePartnerDetails}" >
        <t:fieldWithLink id="care_you_provide_name" name="preview.fullName.label" value="careeFullName" displayChangeButton="${displayChangeButton}" link="${careeFullNameLink}" />
        <t:fieldWithLink id="care_you_provide_dob" name="careeDateOfBirth.label" value="cads:dateOffset(careeDateOfBirth_day, careeDateOfBirth_month, careeDateOfBirth_year, 'dd MMMMMMMMMM, yyyy', '')" displayChangeButton="${displayChangeButton}" link="${careeDateOfBirthLink}" />
    </c:if>
    <t:fieldWithLink id="care_you_provide_relationship" name="careeRelationship.label" value="careeRelationship" displayChangeButton="${displayChangeButton}" link="${careeRelationshipLink}" />
    <t:fieldWithLink id="care_you_provide_address" name="careeSameAddress.label" value="careeSameAddressWithPostcode" displayChangeButton="${displayChangeButton}" link="${careeAddressLink}" />
    <%-- Commented out until new page UC question converted into C3 Java
    <t:fieldWithLink id="care_you_provide_spent35HoursCaring", messages("spent35HoursCaring",dpname), messages("label."+aboutTheCare.spent35HoursCaring.getOrElse("")),disableLink = disableChangeButton, elementId = Some("spent35HoursCaring")) />
    <c:if test="${isOriginGB}" >
        <t:fieldWithLink id="care_you_provide_otherCarer", messages("otherCarer",dpname), messages("label."+aboutTheCare.otherCarer.getOrElse("")),disableLink = disableChangeButton, elementId = Some("otherCarer")) />
        <c:if test="${aboutTheCare.equals(yes)}" >
            <t:fieldWithLink id="care_you_provide_otherCarerUc" name="messages("otherCarerUc",dpname), messages("label."+aboutTheCare.otherCarerUc.getOrElse("")),disableLink = disableChangeButton, elementId = Some("otherCarerUc")) />
        </c:if>
        <c:if test="${aboutTheCare.otherCarerUc.equals(yes)}" >
            <t:fieldWithLink id="care_you_provide_otherCarerUcDetails" name="otherCarerUcDetails", otherCarerUcDetailsText, disableLink = disableChangeButton, elementId = Some("otherCarerUcDetails")) />
        </c:if>
    </c:if>
    --%>
</t:accordion>