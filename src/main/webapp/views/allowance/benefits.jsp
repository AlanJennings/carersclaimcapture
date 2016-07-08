<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">
    <t:pageContent errors="${validationErrors}" pageTitle="Can you get Carer's Allowance?">
        <t:radiobuttons id="benefitsAnswer" 
                        name="benefitsAnswer" 
                        optionValues="PIP|DLA|AA|CAA|AFIP|NONE"
                        optionLabels="Personal Independence Payment (PIP) daily living component|
                                      Disability Living Allowance (DLA) - middle or highest care rate|
                                      Attendance Allowance (AA)|
                                      Constant Attendance Allowance (CAA)|
                                      Armed Forces Independence Payment (AFIP)|
                                      None of these benefits"
                        value="${benefitsAnswer}"
                        label="What benefit does the person you care for get?" 
                        hintBefore="Don't include any benefits they've applied for if they haven't got a decision yet."
                        errors="${validationErrors}" 
        />
        
        <t:hiddenWarning id="answerNoMessageWrap" triggerId="benefitsAnswer" triggerValue="NONE">
            <p>You'll only get Carer's Allowance if the person you care for gets one of these benefits.</p>
        </t:hiddenWarning>
    
    </t:pageContent>
    
    <script type="text/javascript">
        $(function () {
            window.trackEvent = function(arg1, arg2) {
                // do nothing
            };
            
            trackEvent("times", "claim - eligibility");
            setCookie("claimeligibility",new Date().getTime());                       
            GOVUK.performance.stageprompt.setupForGoogleAnalytics()
        });
    </script>
    
</t:mainPage>
