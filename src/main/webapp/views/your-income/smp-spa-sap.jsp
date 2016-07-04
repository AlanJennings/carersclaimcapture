<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Statutory Pay" section="Section 9 of 11" backLink="${previousPage}">
        
        <t:radiobuttons id="otherStatPaymentPaymentTypesForThisPay" 
                        name="otherStatPaymentPaymentTypesForThisPay" 
                        optionValues="MaternityOrPaternityPay|AdoptionPay"
                        optionLabels="Maternity or Paternity Pay|Adoption Pay"
                        value="${otherStatPaymentPaymentTypesForThisPay}"
                        label="Which are you paid?" 
                        errors="${validationErrors}" 
        />
        
        <t:yesnofield id="otherStatPaymentStillBeingPaidThisPay" 
                      name="otherStatPaymentStillBeingPaidThisPay" 
                      value="${otherStatPaymentStillBeingPaidThisPay}"
                      label="Are you still being paid this?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="otherStatPaymentStillBeingPaidThisPayWrap" triggerId="otherStatPaymentStillBeingPaidThisPay" triggerValue="no">
            <t:datefield id="otherStatPaymentWhenDidYouLastGetPaid" 
                         nameDay="otherStatPaymentWhenDidYouLastGetPaid_day" 
                         nameMonth="otherStatPaymentWhenDidYouLastGetPaid_month" 
                         nameYear="otherStatPaymentWhenDidYouLastGetPaid_year" 
                         valueDay="${otherStatPaymentWhenDidYouLastGetPaid_day}" 
                         valueMonth="${otherStatPaymentWhenDidYouLastGetPaid_month}" 
                         valueYear="${otherStatPaymentWhenDidYouLastGetPaid_year}" 
                         label="When were you last paid?" 
                         errors="${validationErrors}" 
            />
        </t:hiddenPanel>
        
        <t:textedit id="otherStatPaymentWhoPaidYouThisPay" name="otherStatPaymentWhoPaidYouThisPay" value="${otherStatPaymentWhoPaidYouThisPay}" maxLength="60" label="Who paid you this?" errors="${validationErrors}" /> 
        <t:textedit id="otherStatPaymentAmountOfThisPay" 
                    name="otherStatPaymentAmountOfThisPay" 
                    value="${otherStatPaymentAmountOfThisPay}" 
                    maxLength="12" 
                    label="Amount paid" 
                    outerClass="short-field"
                    errors="${validationErrors}" 
         />
         
         <t:radiobuttons id="otherStatPaymentHowOftenPaidThisPay" 
                         name="otherStatPaymentHowOftenPaidThisPay" 
                         optionValues="Weekly|Fortnightly|Four-weekly|Monthly|Other"
                         optionLabels="Weekly|
                                       Fortnightly|
                                       Four-weekly|
                                       Monthly|
                                       It varies"
                         value="${otherStatPaymentHowOftenPaidThisPay}"
                         label="How often are you paid?" 
                         errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="otherStatPaymentHowOftenPaidThisPayWrap" triggerId="otherStatPaymentHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit id="otherStatPaymentHowOftenPaidThisPayOther" name="otherStatPaymentHowOftenPaidThisPayOther" value="${otherStatPaymentHowOftenPaidThisPayOther}" maxLength="60" label="How often are you paid?" errors="${validationErrors}" /> 
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
