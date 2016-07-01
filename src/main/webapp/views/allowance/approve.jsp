<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">
    <t:pageContent errors="${validationErrors}" pageTitle="Can you get Carer's Allowance?" backLink="${previousPage}">

        <input type="hidden" id="allowedToContinue" name="allowedToContinue" value="true"/>
        <input type="hidden" id="js" name="jsEnabled" value="false">
        
        <t:htmlsection id="heading1" label="Based on your answers, you may get Carer's Allowance if:"> 
            <ul class="list-bullet">
                <li>your income is no more than &pound;110 a week (after tax and certain expenses)</li>
                <li>you study less than 21 hours a week</li>
            </ul>
        </t:htmlsection> 
        
        <t:htmlsection id="headingDocumentation" label="To make your application you'll need:">
            <ul class="list-bullet">
                <li>your own and your partner&rsquo;s National Insurance number</li>
                <li>bank details</li>
                <li>your latest payslip or P45 if you’ve recently finished working</li>
                <li>the National Insurance number or Disability Living Allowance reference of the person you care for</li>
            </ul> 
        </t:htmlsection>

    </t:pageContent>
    
    <script type="text/javascript">$($("#js").val("true"));</script>
    <script type="text/javascript">
        function analyticsCallback() {
                trackEvent("${currentPage}", "Eligibility - Continue")
        }
    </script>
                
</t:mainPage>