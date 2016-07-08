<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Direct payments for caring for people" section="Section 9 of 11" backLink="${previousPage}">

        <t:yesnofield id="directPaymentStillBeingPaidThisPay" 
                      name="directPaymentStillBeingPaidThisPay" 
                      value="${directPaymentStillBeingPaidThisPay}"
                      label="Are you still being paid this?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="directPaymentStillBeingPaidThisPayWrap" triggerId="directPaymentStillBeingPaidThisPay" triggerValue="no">
            <t:datefield id="directPaymentWhenDidYouLastGetPaid" 
                         nameDay="directPaymentWhenDidYouLastGetPaid_day" 
                         nameMonth="directPaymentWhenDidYouLastGetPaid_month" 
                         nameYear="directPaymentWhenDidYouLastGetPaid_year" 
                         valueDay="${directPaymentWhenDidYouLastGetPaid_day}" 
                         valueMonth="${directPaymentWhenDidYouLastGetPaid_month}" 
                         valueYear="${directPaymentWhenDidYouLastGetPaid_year}" 
                         label="When were you last paid?" 
                         errors="${validationErrors}" 
            />
        </t:hiddenPanel>
        
        <t:textedit id="directPaymentWhoPaidYouThisPay" name="directPaymentWhoPaidYouThisPay" value="${whoPaidYouThisPay}" maxLength="60" label="Who paid you this?" errors="${validationErrors}" /> 
        <t:textedit id="directPaymentAmountOfThisPay" 
                    name="directPaymentAmountOfThisPay" 
                    value="${directPaymentAmountOfThisPay}" 
                    maxLength="12" 
                    label="Amount paid" 
                    outerClass="short-field"
                    errors="${validationErrors}" 
         />
         
         <t:radiobuttons id="directPaymentHowOftenPaidThisPay" 
                         name="directPaymentHowOftenPaidThisPay" 
                         optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other"
                         optionLabels="Weekly|
                                       Fortnightly|
                                       Four-weekly|
                                       Monthly|
                                       It varies"
                         value="${directPaymentHowOftenPaidThisPay}"
                         label="How often are you paid?" 
                         errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="directPaymentHowOftenPaidThisPayWrap" triggerId="directPaymentHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit id="directPaymentHowOftenPaidThisPayOther" name="directPaymentHowOftenPaidThisPayOther" value="${directPaymentHowOftenPaidThisPayOther}" maxLength="60" label="How often are you paid?" errors="${validationErrors}" /> 
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
