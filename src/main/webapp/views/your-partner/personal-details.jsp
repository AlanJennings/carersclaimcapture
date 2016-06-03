<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Partner details - About your partner" section="Section 5 of 11" backLink="${previousPage}">
    
        <t:pre>
            <p>We look at any money your partner gets from benefits when we work out if you can get Carer's Allowance.</p>
        </t:pre>
        
        <t:yesnofield id="hadPartnerSinceClaimDate" 
                          name="hadPartnerSinceClaimDate" 
                          value="${hadPartnerSinceClaimDate}"
                          label="Have you lived with a partner at any time since your claim date?" 
                          hintBefore="<div class="prompt">
                                          <p>Your claim date is 3 June 2016. By partner we mean:</p>
                                          <ul class="list-bullet">
                                              <li>a person you're married to or live with as if you are married.</li>
                                              <li>a civil partner or a person you live with as if you're civil partners.</li>
                                          </ul>
                                      </div>"
                          errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="partnerDetailsWrap" triggerId="hadPartnerSinceClaimDate" triggerValue="yes">

            <t:textedit id="title" name="title" value="${title}" maxLength="20" label="Title" errors="${validationErrors}" /> 
            <t:textedit id="firstName" name="firstName" value="${firstName}" maxLength="17" label="First name" errors="${validationErrors}" />
            <t:textedit id="middleName" name="middleName" value="${middleName}" maxLength="17" label="Middle name(s)" errors="${validationErrors}" />
            <t:textedit id="surname" name="surname" value="${surname}" maxLength="35" label="Last name" errors="${validationErrors}" />
            <t:textedit id="otherNames" name="otherNames" value="${otherNames}" maxLength="35" label="Other surname or maiden name (optional)" errors="${validationErrors}" />
            
            <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
            <t:textedit id="nationalInsuranceNumber" 
                        name="nationalInsuranceNumber" 
                        value="${nationalInsuranceNumber}" 
                        maxLength="19" 
                        label="National Insurance number" 
                        errors="${validationErrors}" 
                        additionalClasses="ni-number"
                        hintBefore='<p class="form-hint">For example, VO123456D</p>'
                        hintAfter='<p class="form-hint">This is on your NI number card, benefit letter, payslip or P60.</p>'
            />
            
            <t:datefield id="dateOfBirth" 
                         nameDay="dateOfBirth_day" 
                         nameMonth="dateOfBirth_month" 
                         nameYear="dateOfBirth_year" 
                         valueDay="${dateOfBirth_day}" 
                         valueMonth="${dateOfBirth_month}" 
                         valueYear="${dateOfBirth_year}" 
                         label="Date of birth" 
                         errors="${validationErrors}" 
                         hintBefore='<p class="form-hint" id="dateOfBirth_defaultDateContextualHelp">For example, 16 5 1976</p>'
            />
            
            <t:textedit id="nationality" name="nationality" value="${nationality}" maxLength="35" label="Nationality" errors="${validationErrors}" />
            
            <t:yesnofield id="separated" 
                          name="separated" 
                          value="${separated}"
                          label="Have you separated since your claim date?" 
                          hintBefore="<p class="form-hint">Your claim date is 3 June 2016.</p>"
                          errors="${validationErrors}" 
            />
            
            <t:yesnofield id="isPartnerPersonYouCareFor" 
                          name="isPartnerPersonYouCareFor" 
                          value="${isPartnerPersonYouCareFor}"
                          label="Is this the person you care for?" 
                          errors="${validationErrors}" 
            />
        </t:hiddenPanel>

    </t:pageContent>
    
    

</t:mainPage>    
