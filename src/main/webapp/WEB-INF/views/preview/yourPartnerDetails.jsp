<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:accordion label="/your-partner/personal-details.pageTitle" openLabel="open-partner" closeLabel="close-partner" track="true">
    <t:fieldWithLink id="partner_hadPartner" name="hadPartnerSinceClaimDate.label" value="hadPartnerSinceClaimDate" displayChangeButton="${displayChangeButton}" link="${hadPartnerSinceClaimDateLink}" />
	<c:if test="${showHadPartnerSinceClaimDate}" >
		<t:fieldWithLink id="partner_name" name="preview.fullName.label" value="partnerDetailsFullName" displayChangeButton="${displayChangeButton}" link="${partnerDetailsFullNameLink}" />
        <t:fieldWithLink id="partner_dateOfBirth" name="partnerDetailsDateOfBirth.label" value="cads:dateOffset(partnerDetailsDateOfBirth_day, partnerDetailsDateOfBirth_month, partnerDetailsDateOfBirth_year, 'dd MMMMMMMMMM, yyyy', '')" displayChangeButton="${displayChangeButton}" link="${partnerDetailsDateOfBirthLink}" />
        <t:fieldWithLink id="partner_nationality" name="partnerDetailsNationality.label" value="partnerDetailsNationality" displayChangeButton="${displayChangeButton}" link="${partnerDetailsNationalityLink}" />
        <t:fieldWithLink id="partner_seperated" name="partnerDetailsSeparated.label" value="partnerDetailsSeparated" displayChangeButton="${displayChangeButton}" link="${partnerDetailsSeparatedLink}" />
        <t:fieldWithLink id="partner_isPersonCareFor" name="isPartnerPersonYouCareFor.label" value="isPartnerPersonYouCareFor" displayChangeButton="${displayChangeButton}" link="${isPartnerPersonYouCareForLink}" />
    </c:if>
</t:accordion>
