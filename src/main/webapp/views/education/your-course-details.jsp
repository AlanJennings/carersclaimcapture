<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Education" section="Section 8 of 11" backLink="${previousPage}">
        <t:yesnofield id="beenInEducationSinceClaimDate" 
                      name="beenInEducationSinceClaimDate" 
                      value="${beenInEducationSinceClaimDate}"
                      label="Have you been on a course of education since your claim date?" 
                      hintBefore="Your claim date is ${dateOfClaim}. If you're on holiday or on temporary leave from a course select 'Yes'"
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="educationWrap" triggerId="beenInEducationSinceClaimDate" triggerValue="yes">
            
            <t:textarea id="courseTitle" 
                        name="courseTitle" 
                        value="${courseTitle}" 
                        maxLength="75" 
                        additionalClasses="textarea-reduced"
                        label="Course title" 
                        errors="${validationErrors}"
            />

            <t:textarea id="nameOfSchoolCollegeOrUniversity" 
                        name="nameOfSchoolCollegeOrUniversity" 
                        value="${nameOfSchoolCollegeOrUniversity}" 
                        maxLength="60" 
                        additionalClasses="textarea-reduced"
                        label="Name of school, college or university" 
                        errors="${validationErrors}"
            />

            <t:textedit id="nameOfMainTeacherOrTutor" 
                        name="nameOfMainTeacherOrTutor" 
                        value="${nameOfMainTeacherOrTutor}" 
                        maxLength="60" 
                        label="Name of main teacher or tutor" 
                        errors="${validationErrors}"
            />

            <t:textedit id="courseContactNumber" 
                        name="courseContactNumber" 
                        value="${courseContactNumber}" 
                        maxLength="20" 
                        label="Course contact number (optional)" 
                        errors="${validationErrors}"
                        hintBefore="Include if you know the number for your department."
            />

            <t:datefield id="educationStartDate" 
                         nameDay="educationStartDate_day" 
                         nameMonth="educationStartDate_month" 
                         nameYear="educationStartDate_year" 
                         valueDay="${educationStartDate_day}" 
                         valueMonth="${educationStartDate_month}" 
                         valueYear="${educationStartDate_year}" 
                         label="When did you start the course?" 
                         hintBefore="For example, 27 9 2015"
                         errors="${validationErrors}" 
            />

            <t:datefield id="educationExpectedEndDate" 
                         nameDay="educationExpectedEndDate_day" 
                         nameMonth="educationExpectedEndDate_month" 
                         nameYear="educationExpectedEndDate_year" 
                         valueDay="${educationExpectedEndDate_day}" 
                         valueMonth="${educationExpectedEndDate_month}" 
                         valueYear="${educationExpectedEndDate_year}" 
                         label="When did the course end or when will you finish?" 
                         hintBefore="For example, 27 6 2016"
                         errors="${validationErrors}" 
            />
            
        </t:hiddenPanel>
    </t:pageContent>

</t:mainPage>    
