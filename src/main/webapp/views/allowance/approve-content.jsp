<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<article class="column-three-quarters main-content eligible">
       <nav class="back-nav" role="navigation">
        <a id="topBackButton" name="topBackButton" href="<c:url value='/allowance/eligibility' />">Back</a>
    </nav>

       <h1 class="form-title heading-large">Can you get Carer's Allowance?</h1>
       
    <section class="prompt e-prompt" aria-labelledby="heading1">
        <h2 class="heading-medium" id="heading1">Based on your answers, you may get Carer's Allowance if:</h2>
        <ul class="list-bullet">
            <li>your income is no more than &pound;110 a week (after tax and certain expenses)</li>
            <li>you study less than 21 hours a week</li>
        </ul>
    </section>

    <section class="prompt e-prompt" aria-labelledby="headingDocumentation">
        <h2 id="headingDocumentation" class="heading-medium">To make your application you'll need:</h2>
        <ul class="list-bullet">
            <li>your own and your partner's National Insurance number</li>
            <li>bank details</li>
            <li>your latest payslip or P45 if you’ve recently finished working</li>
            <li>the National Insurance number or Disability Living Allowance reference of the person you care for</li>
        </ul>
    </section>

    <form action="<c:url value='/allowance/approve' />" method="POST" role="form">                                
        <input type="hidden" name="csrfToken" value="eaf838375986f11252339cd9e8a7382175350433-1461576696564-37e54fc00423766525e2af12"/>    
        <input type="hidden" id="allowedToContinue" name="allowedToContinue" value="true"/>
        <input type="hidden" id="js" name="jsEnabled" value="false">
    
        <nav class="form-steps" role="navigation">
            <ul>
                <li>
                    <button type="submit" 
                            id="next" 
                            name="action" 
                            value="next" 
                            class="button"  
                            onmousedown="trackEvent(&#x27;/allowance/approve&#x27;,&#x27;Eligibility - Yes - Continue&#x27;);" 
                            onkeydown="trackEvent(&#x27;/allowance/approve&#x27;,&#x27;Eligibility - Yes - Continue&#x27;);"
                    >Continue</button>
                 </li>
                
                <li>
                       <a id="bottomBackButton" name="bottomBackButton" href="<c:url value='/allowance/eligibility' />">Back</a>
                </li>
            </ul>
        </nav>
    </form>

    <script type="text/javascript">$($("#js").val("true"));</script>
    <script type="text/javascript">
        function analyticsCallback() {
            
                trackEvent("/allowance/approve", "Eligibility - Continue")
            
        }
    </script>

    <script type="text/javascript" src="<c:url value='/assets/javascript/s_eligibility/g_approve.js' />" ></script>

    <script type="text/javascript">
        $(function(){
        window.initialButtonState("answer_yes",
        "answer_no");
        });

        $(function(){
        window.answer("answer_yes",
        "answer_no");
        });
    </script>
</article>            
