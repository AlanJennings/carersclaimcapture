<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- <c:out value="${errors}"/> -->

<t:mainPage>
    <article class="column-three-quarters main-content eligible">
        <h1 class="form-title heading-large">
            Can you get Carer's Allowance?
        </h1>
        
        <form action="<c:url value='${currentPage}' />" method="POST" role="form">
            <input type="hidden" name="csrfToken" value="1b8e21408cf8c53aeaf62e523f4af3e8b7fe4b6a-1460720627654-dfd779df76ec152c0ae4d491"/>    
            <jsp:include page="../validationSummary.jsp" />
            <fieldset class="form-elements form-eligibility">
                <ul>
                    
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
                    
                </ul>
            </fieldset>
    
            <nav class="form-steps" role="navigation">
                <ul>
                    <li><button type="submit" name="action" value="next" class="button">Next</button></li>
                </ul>
            </nav>   
        </form>
        
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
            
    </article>
</t:mainPage>        


