<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:accordion label="page.where-you-live.text" openLabel="open-nationality" closeLabel="close-nationality" track="true">
    <t:fieldWithLink id="about_you_nationality" name="actualnationality.label" value="${previewNationality}" displayChangeButton="${displayChangeButton}" link="${nationalityLink}" />
    <t:fieldWithLink id="about_you_alwaysliveinuk" name="alwaysLivedInUK.label" value="${alwaysLivedInUK}" displayChangeButton="${displayChangeButton}" link="${alwaysLivedInUKLink}" />
    <c:if test="${showLiveInUKNow}" >
        <t:fieldWithLink id="about_you_liveinuknow" name="liveInUKNow.label" value="${liveInUKNow}" displayChangeButton="${displayChangeButton}" link="${liveInUKNowLink}" />
    </c:if>
    <c:if test="${showArrivedInUK}" >
        <t:fieldWithLink id="about_you_arrivedinuk" name="arrivedInUK.label" value="${arrivedInUK}" displayChangeButton="${displayChangeButton}" link="${arrivedInUKLink}" />
    </c:if>
    <c:if test="${not empty arrivedInUKDate_day}" >
        <t:fieldWithLink id="about_you_arrivedinukdate" name="arrivedInUKDate.label" value="${cads:dateOffset(arrivedInUKDate_day, arrivedInUKDate_month, arrivedInUKDate_year, 'dd MMMMMMMMMM, yyyy', '')}" displayChangeButton="${displayChangeButton}" link="${arrivedInUKDateLink}" />
        <t:fieldWithLink id="about_you_arrivedinukfrom" name="arrivedInUKFrom.label" value="${arrivedInUKFrom}" displayChangeButton="${displayChangeButton}" link="${arrivedInUKFromLink}" />
    </c:if>
    <t:fieldWithLink id="about_you_trip52weeks" name="trip52Weeks.label" value="${trip52Weeks}" displayChangeButton="${displayChangeButton}" link="${trip52weeksLink}" />
    <t:fieldWithLink id="about_you_eeaGuardQuestion" name="eeaGuardQuestion.label" value="${eeaGuardQuestion}" displayChangeButton="${displayChangeButton}" link="${eeaGuardQuestionLink}" />
    <c:if test="${showEEA}" >
        <t:fieldWithLink id="about_you_benefitsFromEEA" name="benefitsFromEEADetails.label" value="${benefitsFromEEADetails}" displayChangeButton="${displayChangeButton}" link="${benefitsFromEEADetailsLink}" />
        <t:fieldWithLink id="about_you_workingForEEA" name="workingForEEADetails.label" value="${workingForEEADetails}" displayChangeButton="${displayChangeButton}" link="${workingForEEADetailsLink}" />
    </c:if>
</t:accordion>