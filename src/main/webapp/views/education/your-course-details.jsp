<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.your-course-details" currentPage="${currentPage}" analytics="true">

    <t:pageContent errors="${validationErrors}" page="page.your-course-details" backLink="${previousPage}">

        <t:yesnofield name="beenInEducationSinceClaimDate" />

        <t:hiddenPanel id="educationWrap" triggerId="beenInEducationSinceClaimDate" triggerValue="yes">
            <t:textarea name="courseTitle" maxLength="75" additionalClasses="textarea-reduced" />
            <t:textarea name="nameOfSchoolCollegeOrUniversity" maxLength="60" additionalClasses="textarea-reduced" />
            <t:textedit name="nameOfMainTeacherOrTutor" maxLength="60" />
            <t:textedit name="courseContactNumber" maxLength="20" />
            <t:datefield name="educationStartDate" />
            <t:datefield name="educationExpectedEndDate" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
