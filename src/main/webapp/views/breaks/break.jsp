<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Breaks from care" section="" backLink="${previousPage}">

        <input type="hidden" id="break_id" name="break_id" value="${break_id}" >
        
        <t:datefield id="startDate" 
                     nameDay="startDate_day" 
                     nameMonth="startDate_month" 
                     nameYear="startDate_year" 
                     valueDay="${startDate_day}" 
                     valueMonth="${startDate_month}" 
                     valueYear="${startDate_year}" 
                     label="When did the break start?" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 3 5 2016"
                     hintBeforeId="start_defaultDateContextualHelp"
        />
        
        <%-- visibility controlled by javascript --%>
        <t:textedit id="startTime" 
                    name="startTime" 
                    value="${startTime}" 
                    maxLength="10" 
                    label="Break start time? (optional)" 
                    errors="${validationErrors}" 
                    hintBefore="For example, 10:00am."
                    outerStyle="display: none;"
        />
        
        <t:radiobuttons id="whereCaree" 
                        name="whereCaree" 
                        optionValues="In_hospital|In_respite_care|On_holiday|At_home|Somewhere_else"
                        optionLabels="in hospital|
                                      in respite care|
                                      on holiday|
                                      at home|
                                      somewhere else"
                        value="${whereCaree}"
                        label="Where was the person you care for during the break?"
                        errors="${validationErrors}" 
                        hintBefore="During this time, they were:" 
        />
        
        <t:hiddenPanel id="wherePersonBreaksInCareWrap" triggerId="whereCaree" triggerValue="Somewhere_else">
            <t:textarea id="whereCareeOtherText"
                        name="whereCareeOtherText"
                        value="${whereCareeOtherText}"
                        maxLength="60"
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <%-- TODO: Why are these in a different order? --%>
        <t:radiobuttons id="whereYou" 
                        name="whereYou" 
                        optionValues="At_home|On_holiday|In_hospital|Somewhere_else"
                        optionLabels="at home|
                                      on holiday|
                                      in hospital|
                                      somewhere else"
                        value="${whereYou}"
                        label="Where were you during the break?"
                        errors="${validationErrors}" 
                        hintBefore="During this time, I was:" 
        />
        
        <t:hiddenPanel id="whereYouBreaksInCareWrap" triggerId="whereYou" triggerValue="Somewhere_else">
            <t:textarea id="whereYouOtherText"
                        name="whereYouOtherText"
                        value="${whereYouOtherText}"
                        maxLength="60"
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>
        
        <t:yesnofield id="hasBreakEnded" name="hasBreakEnded" value="${hasBreakEnded}" label="Has this break ended?" errors="${validationErrors}" />
        
        <t:hiddenPanel id="hasBreakEndedWrap" triggerId="hasBreakEnded" triggerValue="yes">
            <t:datefield id="hasBreakEndedDate" 
                         nameDay="hasBreakEndedDate_day" 
                         nameMonth="hasBreakEndedDate_month" 
                         nameYear="hasBreakEndedDate_year" 
                         valueDay="${hasBreakEndedDate_day}" 
                         valueMonth="${hasBreakEndedDate_month}" 
                         valueYear="${hasBreakEndedDate_year}" 
                         label="When did the break end?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 10 5 2016"
                         hintBeforeId="hasBreakEnded_date_defaultDateContextualHelp"
            />
        </t:hiddenPanel>
        
        <t:yesnofield id="medicalCareDuringBreak" name="medicalCareDuringBreak" value="${medicalCareDuringBreak}" label="Did you or the person you care for get any medical treatment or professional care during this time?" errors="${validationErrors}" />

    </t:pageContent>

</t:mainPage>    
