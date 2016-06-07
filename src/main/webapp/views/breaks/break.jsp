<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Breaks from care" section="" backLink="${previousPage}">

        <t:datefield id="start" 
                     nameDay="start_day" 
                     nameMonth="start_month" 
                     nameYear="start_year" 
                     valueDay="${start_day}" 
                     valueMonth="${start_month}" 
                     valueYear="${start_year}" 
                     label="When did the break start?" 
                     errors="${validationErrors}" 
                     hintBefore='<p class="form-hint" id="start_defaultDateContextualHelp">For example, 3 5 2016</p>'
        />
        
        <%-- TODO Should this be a foldout? --%>
        <t:textedit id="startTime" 
                    name="startTime" 
                    value="${startTime}" 
                    maxLength="10" 
                    label="Break start time? (optional)" 
                    errors="${validationErrors}" 
                    hintBefore='<p class="form-hint">For example, 10:00am.</p>'
                    outerStyle="display: none;"
        />
        
        <t:radiobuttons id="wherePerson" 
                        name="wherePerson" 
                        optionIds="In hospital|In respite care|On holiday|At home|Somewhere else"
                        optionValues="in hospital|
                                      in respite care|
                                      on holiday|
                                      at home|
                                      somewhere else"
                        value="${wherePerson}"
                        label="Where was the person you care for during the break?"
                        errors="${validationErrors}" 
                        hintBefore='<p class="form-hint">During this time, they were:</p>' 
        />
        
        <t:hiddenPanel id="wherePersonBreaksInCareWrap" triggerId="wherePerson" triggerValue="somewhere else">
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
                        optionIds="At home|On holiday|In hospital|Somewhere else"
                        optionValues="at home
                                      on holiday
                                      in hospital
                                      somewhere else"
                        value="${whereYou}"
                        label="Where were you during the break?"
                        errors="${validationErrors}" 
                        hintBefore='<p class="form-hint">During this time, I was:</p>' 
        />
        
        <t:hiddenPanel id="whereYouBreaksInCareWrap" triggerId="whereYou" triggerValue="somewhere else">
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
                         hintBefore='<p class="form-hint" id="hasBreakEnded_date_defaultDateContextualHelp">For example, 10 5 2016</p>'
            />
        </t:hiddenPanel>
        
        <t:yesnofield id="medicalDuringBreak" name="medicalDuringBreak" value="${medicalDuringBreak}" label="Did you or the person you care for get any medical treatment or professional care during this time?" errors="${validationErrors}" />

    </t:pageContent>

</t:mainPage>    
