<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="${employerName}" section="Section 9 of 11" backLink="${previousPage}">
        <input type="hidden" id="employment_id" name="employment_id" value="${employment_id}" >
        <input type="hidden" id="employerName" name="employerName" value="${employerName}" >

        <t:select id="paymentFrequency" 
                  name="paymentFrequency" 
                  optionValues="|Single|Weekly|Fortnightly|Four-weekly|Monthly|Other"
                  optionLabels="Select|
                                Weekly|
                                Fortnightly|
                                Four-weekly|
                                Monthly|
                                Other"
                  value="${paymentFrequency}"
                  label="How often are you paid?" 
                  errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="paymentFrequencyOtherWrap" triggerId="paymentFrequency" triggerValue="Other">
            <t:textarea id="paymentFrequencyOtherText" 
                        name="paymentFrequencyOtherText" 
                        value="${paymentFrequencyOtherText}" 
                        maxLength="60" 
                        showRemainingChars="true"
                        additionalClasses="textarea-reduced"
                        label="How often are you paid? (optional)" 
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <t:textedit id="whenGetPaid" name="whenGetPaid" value="${whenGetPaid}" maxLength="60" label="When do you get paid?" hintBefore="For example, day of the month." errors="${validationErrors}" />

        <t:datefield id="lastPaidDate" 
                     nameDay="lastPaidDate_day" 
                     nameMonth="lastPaidDate_month" 
                     nameYear="lastPaidDate_year" 
                     valueDay="${lastPaidDate_day}" 
                     valueMonth="${lastPaidDate_month}" 
                     valueYear="${lastPaidDate_year}" 
                     label="When were you last paid?" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 10 6 2016"
        />

        <t:textedit id="grossPay" 
                    name="grossPay" 
                    value="${grossPay}" 
                    maxLength="12" 
                    label="What were you paid in your last wage?" 
                    hintBefore="This is the amount you received before tax and deductions."
                    errors="${validationErrors}" />
        
        <t:textarea id="payInclusions" 
                    name="payInclusions" 
                    value="${payInclusions}" 
                    maxLength="300" 
                    showRemainingChars="true"
                    label="What was included in this pay? (optional)" 
                    hintBefore="For example, holiday, sick, or any redundancy pay."
                    errors="${validationErrors}"
        />
        
        <t:yesnofield id="sameAmountEachTime" 
                      name="sameAmountEachTime" 
                      value="${sameAmountEachTime}"
                      label="Do you get the same amount each time?" 
                      errors="${validationErrors}" 
        />
        
    </t:pageContent>

</t:mainPage>    

