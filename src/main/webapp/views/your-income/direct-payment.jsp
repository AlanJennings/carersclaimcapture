<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Direct payments for caring for people" section="Section 9 of 11" backLink="${previousPage}">

        <t:yesnofield id="stillBeingPaidThisPay" 
                      name="stillBeingPaidThisPay" 
                      value="${stillBeingPaidThisPay}"
                      label="Are you still being paid this?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="stillBeingPaidThisPayWrap" triggerId="stillBeingPaidThisPay" triggerValue="no">
            <t:datefield id="whenDidYouLastGetPaid" 
                         nameDay="whenDidYouLastGetPaid_day" 
                         nameMonth="whenDidYouLastGetPaid_month" 
                         nameYear="whenDidYouLastGetPaid_year" 
                         valueDay="${whenDidYouLastGetPaid_day}" 
                         valueMonth="${whenDidYouLastGetPaid_month}" 
                         valueYear="${whenDidYouLastGetPaid_year}" 
                         label="When were you last paid?" 
                         errors="${validationErrors}" 
            />
        </t:hiddenPanel>
        
        <t:textedit id="whoPaidYouThisPay" name="whoPaidYouThisPay" value="${whoPaidYouThisPay}" maxLength="60" label="Who paid you this?" errors="${validationErrors}" /> 
        <t:textedit id="amountOfThisPay" 
                    name="amountOfThisPay" 
                    value="${amountOfThisPay}" 
                    maxLength="12" 
                    label="Amount paid" 
                    outerClass="short-field"
                    errors="${validationErrors}" 
         />
         
         <t:radiobuttons id="howOftenPaidThisPay" 
                         name="howOftenPaidThisPay" 
                         optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other"
                         optionLabels="Weekly|
                                       Fortnightly|
                                       Four-weekly|
                                       Monthly|
                                       It varies"
                         value="${howOftenPaidThisPay}"
                         label="How often are you paid?" 
                         errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="howOftenPaidThisPayWrap" triggerId="howOftenPaidThisPay" triggerValue="Other">
            <t:textedit id="howOftenPaidThisPayOther" name="howOftenPaidThisPayOther" value="${howOftenPaidThisPayOther}" maxLength="60" label="How often are you paid?" errors="${validationErrors}" /> 
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
