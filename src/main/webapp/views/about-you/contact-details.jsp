<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="About you - the carer" section="Section 3 of 11" backLink="${previousPage}">
        
    public static final String CARER_ADDRESS_LINE_ONE           = "";
    public static final String CARER_ADDRESS_LINE_TWO           = "";
    public static final String CARER_ADDRESS_LINE_THREE         = "";
    public static final String CARER_POSTCODE                   = "";
    public static final String CARER_HOW_WE_CONTACT_YOU         = "";
    public static final String CARER_HONTACT_YOU_BY_TELEPHONE   = "";
    public static final String CARER_WANTS_EMAIL_CONTACT        = "";
    public static final String CARER_MAIL                       = "";
    public static final String CARER_MAIL_CONFIRMATION          = "";
        <t:address id="address" 
                   nameOne="carerAddressLineOne"
                   nameTwo="carerAddressLineTwo"
                   nameThree="carerAddressLineThree"
                   valueOne="${carerAddressLineOne}"
                   valueTwo="${carerAddressLineTwo}"
                   valueThree="${carerAddressLineThree}"
                   maxlength="35"
                   label="Address"
                   errors="${validationErrors}"
        />
        
        <t:textedit id="carerPostcode" name="carerPostcode" value="${carerPostcode}" maxLength="10" label="Postcode" errors="${validationErrors}" />
        
        <t:textedit id="carerHowWeContactYou" name="carerHowWeContactYou" value="${carerHowWeContactYou}" maxLength="20" label="Contact number" errors="${validationErrors}" />
        
        <t:checkbox id="contactYouByTextphone" 
                    name="carerContactYouByTelephone" 
                    checkedValue="yes" 
                    value="${carerContactYouByTelephone}"
                    text="This is a textphone for people with hearing difficulties." 
                    additionalClasses="checkbox-single"
                    outerClass="form-group checkbox-single" 
                    blockLabel="false"
                    errors="${validationErrors}"
        />
        
        <t:yesnofield id="carerWantsEmailContact" 
                      name="carerWantsEmailContact" 
                      value="${wantsEmailContact}"
                      label="Do you want an email to confirm your application has been received?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="emailWrap" triggerId="carerWantsEmailContact" triggerValue="yes">
            <t:textedit id="carerMail" name="carerMail" value="${carerMail}" maxLength="254" label="Your email address" errors="${validationErrors}" /> 
            <t:textedit id="carerMailConfirmation" name="carerMailConfirmation" value="${carerMailConfirmation}" maxLength="254" label="Confirm your email address" errors="${validationErrors}" /> 
        </t:hiddenPanel>
        
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/trackInputOnEvent.js' />"></script>
    
    <script type="text/javascript">
        $(function(){        
                window.trackInputOnEventInit('carerHowWeContactYou', "carerHowWeContactYou", 'button');
        });
    </script>

    <script type="text/javascript">
        $ ( function ( ) {
            GOVUK.performance.stageprompt.setupForGoogleAnalytics();
            ;
        } )
    </script>

</t:mainPage>    
