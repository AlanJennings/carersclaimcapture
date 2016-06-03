<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="About you - the carer" section="Section 3 of 11" backLink="${previousPage}">
        
        <t:address id="address" 
                   nameOne="address_lineOne"
                   nameTwo="address_lineTwo"
                   nameThree="address_lineThree"
                   valueOne="${address_lineOne}"
                   valueTwo="${address_lineTwo}"
                   valueThree="${address_lineThree}"
                   maxlength="35"
                   label="Address"
                   hasError="${validationErrors.hasError('address')}" 
                   errorMessage="${validationErrors.getErrorMessage('address')}"
        />
        
        <t:textedit id="postcode" name="postcode" value="${postcode}" maxLength="10" label="Postcode" hasError="${validationErrors.hasError('postcode')}" errorMessage="${validationErrors.getErrorMessage('postcode')}" />
        
        <t:textedit id="howWeContactYou" name="howWeContactYou" value="${howWeContactYou}" maxLength="20" label="Contact number" hasError="${validationErrors.hasError('howWeContactYou')}" errorMessage="${validationErrors.getErrorMessage('howWeContactYou')}" />
        <t:checkbox id="contactYouByTextphone" 
                    name="contactYouByTextphone" 
                    checkedValue="true" 
                    value="${contactYouByTextphone}"
                    text="This is a textphone for people with hearing difficulties." 
                    additionalClasses="checkbox-single"
                    outerClass="form-group checkbox-single" 
                    hasError="${validationErrors.hasError('contactYouByTextphone')}" 
                    errorMessage="${validationErrors.getErrorMessage('contactYouByTextphone')}"
        />
        
        <t:yesnofield id="wantsEmailContact" 
                      name="wantsEmailContact" 
                      value="${wantsEmailContact}"
                      label="Do you want an email to confirm your application has been received?" 
                      hasError="${validationErrors.hasError('wantsEmailContact')}" 
                      errorMessage="${validationErrors.getErrorMessage('wantsEmailContact')}" 
        />
        
        <li id="emailWrap" class="form-group" aria-hidden="false" style="display: none;">
            <ul class="extra-group">
                <t:textedit id="mail" name="mail" value="${mail}" maxLength="254" label="Your email address" hasError="${validationErrors.hasError('mail')}" errorMessage="${validationErrors.getErrorMessage('mail')}" /> 
                <t:textedit id="mailConfirmation" name="mailConfirmation" value="${mailConfirmation}" maxLength="254" label="Confirm your email address" hasError="${validationErrors.hasError('mailConfirmation')}" errorMessage="${validationErrors.getErrorMessage('mailConfirmation')}" /> 
            </ul>
        </li>
        
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/email.js' />"></script>
    <script type="text/javascript" src="<c:url value='/assets/javascript/trackInputOnEvent.js' />"></script>
    
    <script type="text/javascript">
        $(function(){
                window.emailInit(
                    "wantsEmailContact_yes",
                    "wantsEmailContact_no",
                    "mail",
                    "mailConfirmation"
                )
    
                window.trackInputOnEventInit('howWeContactYou', "howWeContactYou", 'button');
        });
    </script>

    <script type="text/javascript">
        $ ( function ( ) {
            GOVUK.performance.stageprompt.setupForGoogleAnalytics();
            ;
        } )
    </script>

</t:mainPage>    
