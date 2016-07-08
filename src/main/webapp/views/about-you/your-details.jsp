<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="About you - the carer" section="Section 3 of 11" backLink="${previousPage}">
    
        <t:textedit id="carerTitle" name="carerTitle" value="${carerTitle}" maxLength="20" label="Title" errors="${validationErrors}" /> 
        <t:textedit id="carerFirstName" name="carerFirstName" value="${carerFirstName}" maxLength="17" label="First name" errors="${validationErrors}" />
        <t:textedit id="carerMiddleName" name="carerMiddleName" value="${carerMiddleName}" maxLength="17" label="Middle name(s)" errors="${validationErrors}" />
        <t:textedit id="carerSurname" name="carerSurname" value="${carerSurname}" maxLength="35" label="Last name" errors="${validationErrors}" />
        
        <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:textedit id="carerNationalInsuranceNumber" 
                    name="carerNationalInsuranceNumber" 
                    value="${carerNationalInsuranceNumber}" 
                    maxLength="19" 
                    label="National Insurance number" 
                    errors="${validationErrors}" 
                    additionalClasses="ni-number"
                    hintBefore="For example, VO123456D"
                    hintAfter="This is on your NI number card, benefit letter, payslip or P60."
        />
        
        <t:datefield id="carerDateOfBirth" 
                     nameDay="carerDateOfBirth_day" 
                     nameMonth="carerDateOfBirth_month" 
                     nameYear="carerDateOfBirth_year" 
                     valueDay="${carerDateOfBirth_day}" 
                     valueMonth="${carerDateOfBirth_month}" 
                     valueYear="${carerDateOfBirth_year}" 
                     label="Date of birth" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 16 5 1976"
                     hintBeforeId="carerDateOfBirth_defaultDateContextualHelp"
        />

    </t:pageContent>
    
    <script type="text/javascript">
        <!-- i think this might relate to save for later -->
        $(document).ready(function(){
            $("#save").click(function(){
                var saveurl=$(this).attr("href");
                var saveurl=$(this).attr("href");
                $("form").attr( "action", saveurl );
                $("form").submit()
            });
        });
    </script>

    <script type="text/javascript">
        $ ( function ( ) {
            GOVUK.performance.stageprompt.setupForGoogleAnalytics();
            ;
        } )
    </script>

</t:mainPage>    
