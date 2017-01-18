<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%--@additionalInfo = @{claim.questionGroup[AdditionalInfo].getOrElse(AdditionalInfo())}--%>
<%--@thirdPartyDetails = @{claim.questionGroup[ThirdPartyDetails].getOrElse(ThirdPartyDetails())}--%>
<%--@aboutYouCarer = @{claim.questionGroup[YourDetails].getOrElse(YourDetails())}--%>
<%--@additionalInfoAnswer = @{--%>
<%--additionalInfo.anythingElse.answer match {--%>
<%--case `yes` => messages("label.yes") + " - " + messages("detailsProvided.simple")--%>
<%--case _ => messages("label.no")--%>
<%--}--%>
<%--}--%>
<%--@additionalInfoWelsh = @{messages("label." + additionalInfo.welshCommunication)}--%>
<%--@thirdPartyDetailsMessage = @{--%>
<%--thirdPartyDetails.thirdParty == ThirdPartyDetails.noCarer match {--%>
<%--case true => messages("thirdParty.completedByThirdParty", thirdPartyDetails.nameAndOrganisation.getOrElse(""), aboutYouCarer.firstName, aboutYouCarer.surname)--%>
<%--case false => messages("thirdParty.completedByCarer", aboutYouCarer.firstName, aboutYouCarer.surname)--%>
<%--}--%>
<%--}--%>

<t:accordion label="/information/additional-info.pageTitle" openLabel="open-additional-info" closeLabel="close-additional-info" track="true">
    <t:fieldWithLink id="additional_info" name="anythingElse.label" value="${additionalInfoAnswer}" displayChangeButton="${displayChangeButton}" link="${additionalInfoAnswerLink}" />
    <c:if test="${languageNotWelsh && isOriginGB}" >
        <t:fieldWithLink id="additional_info_welsh" name="welshCommunication.label" value="${additionalInfoWelsh}" displayChangeButton="${displayChangeButton}" link="${additionalInfoWelshLink}" />
    </c:if>
    <t:fieldWithLink id="third_party" name="preview.thirdParty.completedOnBehalf" value="${thirdPartyDetailsMessage}" displayChangeButton="${displayChangeButton}" link="${thirdPartyDetailsMessageLink}" />
</t:accordion>

