<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:accordion label="/information/additional-info.pageTitle" openLabel="open-additional-info" closeLabel="close-additional-info" track="true">
    <t:fieldWithLink id="additional_info" name="anythingElse.label" value="${additionalInfoAnswer}" displayChangeButton="${displayChangeButton}" link="${additionalInfoAnswerLink}" />
    <c:if test="${languageNotWelsh && isOriginGB}" >
        <t:fieldWithLink id="additional_info_welsh" name="welshCommunication.label" value="${additionalInfoWelsh}" displayChangeButton="${displayChangeButton}" link="${additionalInfoWelshLink}" />
    </c:if>
    <t:fieldWithLink id="third_party" name="preview.thirdParty.completedOnBehalf" value="${thirdPartyDetailsMessage}" useMessageForValue="true" displayChangeButton="${displayChangeButton}" link="${thirdPartyDetailsMessageLink}" />
</t:accordion>

