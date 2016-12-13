<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 
 
<!DOCTYPE html>

<t:mainPage analytics="true">
 
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        
        <t:yesnofield name="beenInEducationSinceClaimDate" />

        <t:hiddenPanel id="educationWrap" triggerId="beenInEducationSinceClaimDate" triggerValue="yes">
            <t:textarea name="courseTitle" additionalClasses="textarea-reduced" />
            <t:textarea name="nameOfSchoolCollegeOrUniversity" additionalClasses="textarea-reduced" />
            <t:textedit name="nameOfMainTeacherOrTutor" />
            <t:textedit name="courseContactNumber" />
            <t:datefield name="educationStartDate" />
            <t:datefield name="educationExpectedEndDate" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
