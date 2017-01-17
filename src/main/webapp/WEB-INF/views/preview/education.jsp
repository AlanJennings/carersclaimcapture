<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:accordion label="/education/your-course-details.pageTitle" openLabel="open-education" closeLabel="close-education" track="true">
    <t:fieldWithLink id="education_beenInEducationSinceClaimDate" name="beenInEducationSinceClaimDate.label" value="${beenInEducationSinceClaimDate}" displayChangeButton="${displayChangeButton}" link="${beenInEducationSinceClaimDateLink}" />
	<c:if test="${showBeenInEducationSinceClaimDate}" >
        <t:fieldWithLink id="education_courseTitle" name="courseTitle.label" value="${courseTitle}" displayChangeButton="${displayChangeButton}" link="${courseTitleLink}" />
        <t:fieldWithLink id="education_nameOfSchool" name="nameOfSchoolCollegeOrUniversity.label" value="${nameOfSchoolCollegeOrUniversity}" displayChangeButton="${displayChangeButton}" link="${nameOfSchoolCollegeOrUniversityLink}" />
        <t:fieldWithLink id="education_nameOfTutor" name="nameOfMainTeacherOrTutor.label" value="${nameOfMainTeacherOrTutor}" displayChangeButton="${displayChangeButton}" link="${nameOfMainTeacherOrTutorLink}" />
        <t:fieldWithLink id="education_contactNumber" name="courseContactNumber.label" value="${courseContactNumber}" displayChangeButton="${displayChangeButton}" link="${courseContactNumberLink}" />
        <t:fieldWithLink id="education_startEndDates" name="preview.startEndDates.label" value="${cads:dateOffset(educationStartDate_day, educationStartDate_month, educationStartDate_year, 'dd MMMMMMMMMM, yyyy', '')} - ${cads:dateOffset(educationExpectedEndDate_day, educationExpectedEndDate_month, educationExpectedEndDate_year, 'dd MMMMMMMMMM, yyyy', '')}" displayChangeButton="${displayChangeButton}" link="${educationStartDateLink}" />
	</c:if>
</t:accordion>