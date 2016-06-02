<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage>
    <t:pageContent pageTitle="About you - the carer" section="Section 3 of 11" backLink="${previousPage}">
    
        <t:textedit id="addressLineOne" name="addressLineOne" value="${addressLineOne}" maxLength="35" label="Address" hasError="${errors.hasError('addressLineOne')}" errorMessage="${errors.getErrorMessage('addressLineOne')}" /> 
        <t:textedit id="addressLineTwo" name="addressLineTwo" value="${addressLineTwo}" maxLength="35" hasError="${errors.hasError('addressLineTwo')}" errorMessage="${errors.getErrorMessage('addressLineTwo')}" />
        <t:textedit id="addressLineThree" name="addressLineThree" value="${addressLineThree}" maxLength="35" hasError="${errors.hasError('addressLineThree')}" errorMessage="${errors.getErrorMessage('addressLineThree')}" />
        <t:textedit id="postcode" name="postcode" value="${postcode}" maxLength="10" label="Postcode" hasError="${errors.hasError('postcode')}" errorMessage="${errors.getErrorMessage('postcode')}" />
        <t:textedit id="howWeContactYou" name="howWeContactYou" value="${howWeContactYou}" maxLength="20" label="Contact number" hasError="${errors.hasError('howWeContactYou')}" errorMessage="${errors.getErrorMessage('howWeContactYou')}" />
        
        <t:checkbox id="contactYouByTextphone" name="contactYouByTextphone" value=${contactYouByTextphone} label="This is a textphone for people with hearing difficulties." />
        
        <t:yesnofield id="wantsEmailContact" 
                      name="wantsEmailContact" 
                      value="${wantsEmailContact}"
                      label="Do you want an email to confirm your application has been received?" 
                      hasError="${errors.hasError('wantsEmailContact')}" 
                      errorMessage="${errors.getErrorMessage('wantsEmailContact')}" 
        />
        
        <li id="emailWrap" class="form-group" aria-hidden="false" style="display: block;">
            <ul class="extra-group">
                <t:textedit id="mail" name="mail" value="${mail}" maxLength="254" label="Your email address" hasError="${errors.hasError('mail')}" errorMessage="${errors.getErrorMessage('mail')}" /> 
                <t:textedit id="mailConfirmation" name="mailConfirmation" value="${mailConfirmation}" maxLength="254" label="Confirm your email address" hasError="${errors.hasError('mailConfirmation')}" errorMessage="${errors.getErrorMessage('mailConfirmation')}" /> 
            </ul>
        </li>
        
    </t:pageContent>
    
    <script type="text/javascript" src="/assets/javascripts/email.js"></script>
    <script type="text/javascript" src="/assets/javascripts/trackInputOnEvent.js"></script>
    
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
