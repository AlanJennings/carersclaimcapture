<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="${employerName}" section="Section 9 of 11" backLink="${previousPage}">

        <t:select id="employmentPaymentFrequency" 
                  name="employmentPaymentFrequency" 
                  optionValues="|Single|Weekly|Fortnightly|Four-weekly|Monthly|Other"
                  optionLabels="Select|
                                Weekly|
                                Fortnightly|
                                Four-weekly|
                                Monthly|
                                Other"
                  value="${employmentPaymentFrequency}"
                  label="How often are you paid?" 
                  errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="employmentPaymentFrequencyOtherWrap" triggerId="employmentPaymentFrequency" triggerValue="Other">
            <t:textarea id="employmentPaymentFrequencyOtherText" 
                        name="employmentPaymentFrequencyOtherText" 
                        value="${employmentPaymentFrequencyOtherText}" 
                        maxLength="60" 
                        showRemainingChars="true"
                        additionalClasses="textarea-reduced"
                        label="How often are you paid? (optional)" 
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <t:textedit id="employmentwhenGetPaid" name="employmentwhenGetPaid" value="${employmentwhenGetPaid}" maxLength="60" label="When do you get paid?" hintBefore="For example, day of the month." errors="${validationErrors}" />

        <t:datefield id="employmentLastPaidDate" 
                     nameDay="employmentLastPaidDate_day" 
                     nameMonth="employmentLastPaidDate_month" 
                     nameYear="employmentLastPaidDate_year" 
                     valueDay="${employmentLastPaidDate_day}" 
                     valueMonth="${employmentLastPaidDate_month}" 
                     valueYear="${employmentLastPaidDate_year}" 
                     label="When were you last paid?" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 10 6 2016"
        />

        <t:textedit id="employmentGrossPay" 
                    name="employmentGrossPay" 
                    value="${employmentGrossPay}" 
                    maxLength="12" 
                    label="What were you paid in your last wage?" 
                    hintBefore="This is the amount you received before tax and deductions."
                    errors="${validationErrors}" />
        
        <t:textarea id="employmentPayInclusions" 
                    name="employmentPayInclusions" 
                    value="${employmentPayInclusions}" 
                    maxLength="300" 
                    showRemainingChars="true"
                    label="What was included in this pay? (optional)" 
                    hintBefore="For example, holiday, sick, or any redundancy pay."
                    errors="${validationErrors}"
        />
        
        <t:yesnofield id="employmentSameAmountEachTime" 
                      name="employmentSameAmountEachTime" 
                      value="${employmentSameAmountEachTime}"
                      label="Do you get the same amount each time?" 
                      errors="${validationErrors}" 
        />
        
    </t:pageContent>

</t:mainPage>    

