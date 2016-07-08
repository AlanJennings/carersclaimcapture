<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="About you - the carerAbout your nationality and where you live - Nationality and where you've lived" section="Section 4 of 11" backLink="${previousPage}">

        <t:radiobuttons id="nationality" 
                        name="nationality" 
                        optionValues="British|Another nationality"
                        optionLabels="British|
                                      Another nationality"
                        value="${nationality}"
                        label="What is your nationality?" 
                        errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="actualNationalityWrap" triggerId="nationality" triggerValue="Another nationality">
            <t:textedit id="actualnationality" name="actualnationality" value="${actualnationality}" maxLength="35" label="Your nationality" errors="${validationErrors}" />
        </t:hiddenPanel>
        
        <t:yesnofield id="alwaysLivedInUK" 
                      name="alwaysLivedInUK" 
                      value="${alwaysLivedInUK}"
                      label="Have you always lived in England, Scotland or Wales?" 
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="alwaysLivedInUKNoWrap" triggerId="alwaysLivedInUK" triggerValue="no">
            <t:yesnofield id="liveInUKNow" 
                          name="liveInUKNow" 
                          value="${liveInUKNow}"
                          label="Do you live in England, Scotland or Wales now?" 
                          errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="liveInUKNowYesWrap" triggerId="liveInUKNow" triggerValue="yes">
                <t:radiobuttons id="arrivedInUK" 
                            name="arrivedInUK" 
                            optionValues="less|more"
                            optionLabels="Less than 3 years ago|
                                          More than 3 years ago"
                            value="${arrivedInUK}"
                            label="When did you arrive in England, Scotland or Wales?" 
                            errors="${validationErrors}" 
                />
                
                <t:hiddenPanel id="arrivedInUKRecentWrap" triggerId="arrivedInUK" triggerValue="less">
                    <t:datefield id="arrivedInUKDate" 
                                 nameDay="arrivedInUKDate_day" 
                                 nameMonth="arrivedInUKDate_month" 
                                 nameYear="arrivedInUKDate_year" 
                                 valueDay="${arrivedInUKDate_day}" 
                                 valueMonth="${arrivedInUKDate_month}" 
                                 valueYear="${arrivedInUKDate_year}" 
                                 label="Date arrived" 
                                 errors="${validationErrors}" 
                    />
                    <t:textedit id="arrivedInUKFrom" name="arrivedInUKFrom" value="${arrivedInUKFrom}" maxLength="60" label="Which country did you live in?" errors="${validationErrors}" />
                </t:hiddenPanel>
            </t:hiddenPanel>
        </t:hiddenPanel>

        <t:yesnofield id="trip52Weeks" 
                      name="trip52Weeks" 
                      value="${trip52Weeks}"
                      label="Have you been away from England, Scotland or Wales for more than 52 weeks in the 3 years before your claim date?" 
                      hintBefore="This could be one trip, or a number of trips adding up to 52 weeks."
                      errors="${validationErrors}" 
        />
        <t:hiddenPanel id="trip52WeeksYesWrap" triggerId="trip52Weeks" triggerValue="yes">
            <t:textarea id="tripDetails"
                        name="tripDetails"
                        value="${tripDetails}"
                        maxLength="3000"
                        showRemainingChars="true"
                        label="Tell us about where you've been." 
                        hintBefore="Include dates you left and returned. We might contact you for further details"
                        errors="${validationErrors}"
            />
            
        </t:hiddenPanel>
        
    </t:pageContent>
    
</t:mainPage>    
