<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--@yourDetails = @{claim.questionGroup[YourDetails].getOrElse(YourDetails())}--%>
<%--@contactDetails = @{claim.questionGroup[ContactDetails].getOrElse(ContactDetails())}--%>
<%--@address = @{contactDetails.address}--%>
<%--@claimDate = @{claim.questionGroup[ClaimDate].getOrElse(ClaimDate())}--%>
<%--@email = @{--%>
    <%--contactDetails.wantsContactEmail match {--%>
        <%--case yesNo if yesNo == yes => messages("label.yes") + " - " + contactDetails.email.getOrElse("")--%>
        <%--case yesNo if yesNo == no => messages("label.no")--%>
        <%--case _ => ""--%>
    <%--}--%>
<%--}--%>
<%--@maritalStatusQG = @{--%>
    <%--claim.questionGroup[MaritalStatus].getOrElse(MaritalStatus())--%>
<%--}--%>
<%--@maritalStatus = @{--%>
    <%--maritalStatusQG.maritalStatus match {--%>
        <%--case Married => messages("maritalStatus.married")--%>
        <%--case Single => messages("maritalStatus.single")--%>
        <%--case Divorced => messages("maritalStatus.divorced")--%>
        <%--case Widowed => messages("maritalStatus.widowed")--%>
        <%--case Separated => messages("maritalStatus.separated")--%>
        <%--case Partner => messages("maritalStatus.livingWithPartner")--%>
        <%--case _ => ""--%>
    <%--}--%>
<%--}--%>
<%--@middleName = @{yourDetails.middleName.map(_+" ").getOrElse("")}--%>
<%--@titleNames = @{--%>
    <%--s"${yourDetails.title} ${yourDetails.firstName} ${middleName}${yourDetails.surname}"--%>
<%--}--%>

<%--@addressPostcode = @{--%>
    <%--s"${address.lineOne.getOrElse("")}${address.lineTwo.fold("")("<br/>"+_)}${address.lineThree.fold("")("<br/>"+_)} ${"<br/>"+contactDetails.postcode.getOrElse("")}"--%>
<%--}--%>
<%--@saveForLaterEnabled=@{--%>
    <%--getBooleanProperty("saveForLaterSaveEnabled")--%>
<%--}--%>
<%--@emailLabel=@{--%>
    <%--if(saveForLaterEnabled) messages("wantsEmailContactNew")--%>
    <%--else messages("wantsEmailContactOld")--%>
<%--}--%>

<t:accordion label="/about-you/your-details.pageTitle" openLabel="open-about-you" closeLabel="close-about-you" track="true">
    <t:fieldWithLink id="about_you_claimDate" name="dateOfClaim.label" value="cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, 'dd MMMMMMMMMM, yyyy', '')" disableLink="true" link="" />
    <t:fieldWithLink id="about_you_marital_status" name="preview.maritalStatus.label" value="maritalStatus" disableLink="false" link="" />
    <t:fieldWithLink id="about_you_full_name" name="preview.fullName.label" value="titleNames" disableLink="false" link="" />
    <t:fieldWithLink id="about_you_dob" name="carerDateOfBirth.label" value="cads:dateOffset(carerDateOfBirth_day, carerDateOfBirth_month, carerDateOfBirth_year, 'dd MMMMMMMMMM, yyyy', '')" disableLink="false" link="" />
    <t:fieldWithLink id="about_you_address" name="carerAddress.label" value="carerPostcode" disableLink="false" link="" />
    <t:fieldWithLink id="about_you_contact" name="carerHowWeContactYou.label" value="contactDetails.howWeContactYou" disableLink="false" link="" />
    <t:fieldWithLink id="about_you_email" name="carerMail.label" value="email" disableLink="false" link="" />
</t:accordion>

