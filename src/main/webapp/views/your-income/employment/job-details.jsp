<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Employer details" section="" backLink="${previousPage}">

        <t:pre><p>These details are used to contact your employer.</p></t:pre>
        
        <t:textedit id="employerName" name="employerName" value="${employerName}" maxLength="60" label="Employer's name" errors="${validationErrors}" />
        <t:textedit id="employmentPhoneNumber" name="employmentPhoneNumber" value="${employmentPhoneNumber}" maxLength="20" label="Contact number" errors="${validationErrors}" />

        <t:address id="employerAddress" 
                   nameOne="employmentAddressLineOne"
                   nameTwo="employmentAddressLineTwo"
                   nameThree="employmentAddressLineThree"
                   valueOne="${employmentAddressLineOne}"
                   valueTwo="${employmentAddressLineTwo}"
                   valueThree="${employmentAddressLineThree}"
                   maxlength="35"
                   label="Address"
                   errors="${validationErrors}"
        />
        
        <t:textedit id="employmentPostcode" name="employmentPostcode" value="${employmentPostcode}" maxLength="10" label="Postcode (optional)" errors="${validationErrors}" />
        
        <t:yesnofield id="startJobBeforeClaimDate" 
                      name="startJobBeforeClaimDate" 
                      value="${startJobBeforeClaimDate}"
                      label="Did you start this job before ${dateOfClaim}?" 
                      hintBefore="This is 1 month before your claim date."
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="startJobBeforeClaimDateWrap" triggerId="startJobBeforeClaimDate" triggerValue="no">
            <t:datefield id="jobStartDate" 
                         nameDay="jobStartDate_day" 
                         nameMonth="jobStartDate_month" 
                         nameYear="jobStartDate_year" 
                         valueDay="${jobStartDate_day}" 
                         valueMonth="${jobStartDate_month}" 
                         valueYear="${jobStartDate_year}" 
                         label="Date last worked" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 16 6 2016"
            />
        </t:hiddenPanel>
        
        <t:yesnofield id="finishedThisJob" 
                      name="finishedThisJob" 
                      value="${finishedThisJob}"
                      label="Have you finished this job?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="finishedThisWrap" triggerId="finishedThisJob" triggerValue="yes">
            <t:datefield id="lastWorkDate" 
                         nameDay="lastWorkDate_day" 
                         nameMonth="lastWorkDate_month" 
                         nameYear="lastWorkDate_year" 
                         valueDay="${lastWorkDate_day}" 
                         valueMonth="${lastWorkDate_month}" 
                         valueYear="${lastWorkDate_year}" 
                         label="Date last worked" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 16 6 2016"
            />
            
            <t:datefield id="p45LeavingDate" 
                         nameDay="p45LeavingDate_day" 
                         nameMonth="p45LeavingDate_month" 
                         nameYear="p45LeavingDate_year" 
                         valueDay="${p45LeavingDate_day}" 
                         valueMonth="${p45LeavingDate_month}" 
                         valueYear="${p45LeavingDate_year}" 
                         label="Leaving date on your P45 (optional)" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 16 6 2016"
            />
        </t:hiddenPanel>

        <t:textedit id="employmentHoursPerWeek" name="employmentHoursPerWeek" value="${employmentHoursPerWeek}" maxLength="5" label="How many hours a week did you normally work?(optional)" errors="${validationErrors}" />
        
    </t:pageContent>

</t:mainPage>    
