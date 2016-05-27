<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage>
    <t:pageContent pageTitle="Can you get Carer's Allowance?">
        <t:radiobuttons id="benefitsAnswer" 
                        name="benefitsAnswer" 
                        optionIds="PIP|DLA|AA|CAA|AFIP|NONE"
                        optionValues="Personal Independence Payment (PIP) daily living component|
                                      Disability Living Allowance (DLA) - middle or highest care rate|
                                      Attendance Allowance (AA)|
                                      Constant Attendance Allowance (CAA)|
                                      Armed Forces Independence Payment (AFIP)|
                                      None of these benefits"
                        value="${benefitsAnswer}"
                        label="What benefit does the person you care for get?" 
                        hintBefore='<p class="form-hint">Don&rsquo;t include any benefits they&rsquo;ve applied for if they haven&rsquo;t got a decision yet.</p>'
                        warningText='
                          <div id="answerNoMessageWrap" class="prompt breaks-prompt validation-summary" style="display: none;">
                              <p>You&rsquo;ll only get Carer&rsquo;s Allowance if the person you care for gets one of these benefits.</p>
                          </div>'
                        hasError="${errors.hasError('benefitsAnswer')}" 
                        errorMessage="${errors.getErrorMessage('benefitsAnswer')}" 
        />
    
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/s_eligibility/answerNoBenefitsMessage.js' />" ></script>
    <script type="text/javascript">
        $(function () {
            window.trackEvent = function(arg1, arg2) {
                // do nothing
            };
            
            window.initEvents("benefitsAnswer", "NONE");
            trackEvent("times", "claim - eligibility");
            setCookie("claimeligibility",new Date().getTime());                       
            GOVUK.performance.stageprompt.setupForGoogleAnalytics()
        });
    </script>
    
</t:mainPage>
