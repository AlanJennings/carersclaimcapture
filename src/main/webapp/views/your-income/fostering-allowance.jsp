<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Fostering Allowance" section="Section 9 of 11" backLink="${previousPage}">

        <t:radiobuttons id="fosteringAllowancePay" 
                        name="fosteringAllowancePay" 
                        optionValues="LocalAuthority|FosteringAllowance|Other"
                        optionLabels="Local Authority|Fostering Agency|Other"
                        value="${fosteringAllowancePay}"
                        label="What type of organisation pays you for Fostering Allowance?" 
                        errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="fosteringAllowancePayWrap" triggerId="fosteringAllowancePay" triggerValue="Other">
            <t:textedit id="fosteringAllowancePayOther" name="fosteringAllowancePayOther" value="${fosteringAllowancePayOther}" maxLength="60" label="Who paid you Fostering Allowance?" errors="${validationErrors}" /> 
        </t:hiddenPanel>
        
        <t:yesnofield id="fosteringAllowanceStillBeingPaidThisPay" 
                      name="fosteringAllowanceStillBeingPaidThisPay" 
                      value="${fosteringAllowanceStillBeingPaidThisPay}"
                      label="Are you still being paid this?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="fosteringAllowanceStillBeingPaidThisPayWrap" triggerId="fosteringAllowanceStillBeingPaidThisPay" triggerValue="no">
            <t:datefield id="fosteringAllowanceWhenDidYouLastGetPaid" 
                         nameDay="fosteringAllowanceWhenDidYouLastGetPaid_day" 
                         nameMonth="fosteringAllowanceWhenDidYouLastGetPaid_month" 
                         nameYear="fosteringAllowanceWhenDidYouLastGetPaid_year" 
                         valueDay="${fosteringAllowanceWhenDidYouLastGetPaid_day}" 
                         valueMonth="${fosteringAllowanceWhenDidYouLastGetPaid_month}" 
                         valueYear="${fosteringAllowanceWhenDidYouLastGetPaid_year}" 
                         label="When were you last paid?" 
                         errors="${validationErrors}" 
            />
        </t:hiddenPanel>
        
        <t:textedit id="fosteringAllowanceWhoPaidYouThisPay" name="fosteringAllowanceWhoPaidYouThisPay" value="${fosteringAllowanceWhoPaidYouThisPay}" maxLength="60" label="Who paid you this?" errors="${validationErrors}" /> 
        
        <t:textedit id="fosteringAllowanceAmountOfThisPay" 
                    name="fosteringAllowanceAmountOfThisPay" 
                    value="${fosteringAllowanceAmountOfThisPay}" 
                    maxLength="12" 
                    label="Amount paid" 
                    outerClass="short-field"
                    errors="${validationErrors}" 
         />
         
         <t:radiobuttons id="fosteringAllowanceHowOftenPaidThisPay" 
                         name="fosteringAllowanceHowOftenPaidThisPay" 
                         optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other"
                         optionLabels="Weekly|
                                       Fortnightly|
                                       Four-weekly|
                                       Monthly|
                                       It varies"
                         value="${fosteringAllowanceHowOftenPaidThisPay}"
                         label="How often are you paid?" 
                         errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="fosteringAllowanceHowOftenPaidThisPayWrap" triggerId="fosteringAllowanceHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit id="fosteringAllowanceHowOftenPaidThisPayOther" name="fosteringAllowanceHowOftenPaidThisPayOther" value="${fosteringAllowanceHowOftenPaidThisPayOther}" maxLength="60" label="How often are you paid?" errors="${validationErrors}" /> 
        </t:hiddenPanel>

    </t:pageContent>
</t:mainPage>
