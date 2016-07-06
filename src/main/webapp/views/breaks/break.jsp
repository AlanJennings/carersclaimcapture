<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Breaks from care" section="" backLink="${previousPage}">

        <input type="hidden" id="break_id" name="break_id" value="${break_id}" >
        
        <t:datefield id="breakStartDate" 
                     nameDay="breakStartDate_day" 
                     nameMonth="breakStartDate_month" 
                     nameYear="breakStartDate_year" 
                     valueDay="${breakStartDate_day}" 
                     valueMonth="${breakStartDate_month}" 
                     valueYear="${breakStartDate_year}" 
                     label="When did the break start?" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 3 5 2016"
                     hintBeforeId="start_defaultDateContextualHelp"
        />
        
        <%-- visibility controlled by javascript --%>
        <t:textedit id="breakStartTime" 
                    name="breakStartTime" 
                    value="${breakStartTime}" 
                    maxLength="10" 
                    label="Break start time? (optional)" 
                    errors="${validationErrors}" 
                    hintBefore="For example, 10:00am."
                    outerStyle="display: none;"
        />
        
        <t:radiobuttons id="breakWhereCaree" 
                        name="breakWhereCaree" 
                        optionValues="in hospital|in respite care|on holiday|at home|somewhere else"
                        optionLabels="in hospital|
                                      in respite care|
                                      on holiday|
                                      at home|
                                      somewhere else"
                        value="${breakWhereCaree}"
                        label="Where was the person you care for during the break?"
                        errors="${validationErrors}" 
                        hintBefore="During this time, they were:" 
        />
        
        <t:hiddenPanel id="wherePersonBreaksInCareWrap" triggerId="breakWhereCaree" triggerValue="somewhere else">
            <t:textarea id="breakWhereCareeOtherText"
                        name="breakWhereCareeOtherText"
                        value="${breakWhereCareeOtherText}"
                        maxLength="60"
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <%-- TODO: Why are these in a different order? --%>
        <t:radiobuttons id="breakWhereYou" 
                        name="breakWhereYou" 
                        optionValues="at home|on holiday|in hospital|somewhere else"                        
                        optionLabels="at home|
                                      on holiday|
                                      in hospital|
                                      somewhere else"
                        value="${breakWhereYou}"
                        label="Where were you during the break?"
                        errors="${validationErrors}" 
                        hintBefore="During this time, I was:" 
        />
        
        <t:hiddenPanel id="whereYouBreaksInCareWrap" triggerId="breakWhereYou" triggerValue="somewhere else">
            <t:textarea id="breakWhereYouOtherText"
                        name="breakWhereYouOtherText"
                        value="${breakWhereYouOtherText}"
                        maxLength="60"
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>
        
        <t:yesnofield id="breakHasBreakEnded" name="breakHasBreakEnded" value="${breakHasBreakEnded}" label="Has this break ended?" errors="${validationErrors}" />
        
        <t:hiddenPanel id="hasBreakEndedWrap" triggerId="breakHasBreakEnded" triggerValue="yes">
            <t:datefield id="breakbreakHasBreakEndedDate" 
                         nameDay="breakHasBreakEndedDate_day" 
                         nameMonth="breakHasBreakEndedDate_month" 
                         nameYear="breakHasBreakEndedDate_year" 
                         valueDay="${breakHasBreakEndedDate_day}" 
                         valueMonth="${breakHasBreakEndedDate_month}" 
                         valueYear="${breakHasBreakEndedDate_year}" 
                         label="When did the break end?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 10 5 2016"
                         hintBeforeId="hasBreakEnded_date_defaultDateContextualHelp"
            />
        </t:hiddenPanel>
        
        <t:yesnofield id="breakMedicalCareDuringBreak" name="breakMedicalCareDuringBreak" value="${breakMedicalCareDuringBreak}" label="Did you or the person you care for get any medical treatment or professional care during this time?" errors="${validationErrors}" />

    </t:pageContent>

</t:mainPage>    
