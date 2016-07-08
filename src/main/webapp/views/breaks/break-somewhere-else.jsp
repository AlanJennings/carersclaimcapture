<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="The care you give - times you've not cared for 35 hours a week" section="Section 6 of 11" backLink="${previousPage}">

        <t:htmlsection>
            <p>
               You told us there was a time you weren't able to care for the person you are 
               claiming Carer's Allowance for 35 hours a week. We need details about the time.
            </p>
        </t:htmlsection>
        
        <t:datefield id="carerSomewhereElseStartDate" 
                     nameDay="carerSomewhereElseStartDate_day" 
                     nameMonth="carerSomewhereElseStartDate_month" 
                     nameYear="carerSomewhereElseStartDate_year" 
                     valueDay="${carerSomewhereElseStartDate_day}" 
                     valueMonth="${carerSomewhereElseStartDate_month}" 
                     valueYear="${carerSomewhereElseStartDate_year}" 
                     label="What date did you stop providing care?" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 3 5 2016"
                     hintBeforeId="start_defaultDateContextualHelp"
        />
        
        <%-- visibility controlled by javascript --%>
        <t:textedit id="carerSomewhereElseStartTime" 
                    name="carerSomewhereElseStartTime" 
                    value="${carerSomewhereElseStartTime}" 
                    maxLength="10" 
                    label="Time stopped" 
                    errors="${validationErrors}" 
                    outerStyle="display: none;"
        />
        
        <t:yesnofield id="carerSomewhereElseEndedTime" 
                      name="carerSomewhereElseEndedTime" 
                      value="${carerSomewhereElseEndedTime}" 
                      label="Have you started providing care again?" 
                      errors="${validationErrors}" 
        />
        
        <t:datefield id="carerSomewhereElseEndDate" 
                     nameDay="carerSomewhereElseEndDate_day" 
                     nameMonth="carerSomewhereElseEndDate_month" 
                     nameYear="carerSomewhereElseEndDate_year" 
                     valueDay="${carerSomewhereElseEndDate_day}" 
                     valueMonth="${carerSomewhereElseEndDate_month}" 
                     valueYear="${carerSomewhereElseEndDate_year}" 
                     label="When did you start providing care again?" 
                     errors="${validationErrors}" 
                     hintBefore="For example, 3 5 2016"
        />
        
        <%-- visibility controlled by javascript --%>
        <t:textedit id="carerSomewhereElseStartTime" 
                    name="carerSomewhereElseStartTime" 
                    value="${carerSomewhereElseStartTime}" 
                    maxLength="10" 
                    label="Time caring started again" 
                    errors="${validationErrors}" 
                    outerStyle="display: none;"
        />
        
        <%-- TODO: Why are these in a different order? --%>
        <t:radiobuttons id="carerSomewhereElseWhereYou" 
                        name="carerSomewhereElseWhereYou" 
                        optionValues="on holiday|at home|somewhere else"                        
                        optionLabels="on holiday|at home|somewhere else"
                        value="${carerSomewhereElseWhereYou}"
                        label="Where were you during this time?"
                        errors="${validationErrors}" 
                        hintBefore="During this time, I was:" 
        />
        
        <t:hiddenPanel id="carerSomewhereElseWhereYouWrap" triggerId="carerSomewhereElseWhereYou" triggerValue="somewhere else">
            <t:textarea id="carerSomewhereElseWhereYouOtherText"
                        name="carerSomewhereElseWhereYouOtherText"
                        value="${carerSomewhereElseWhereYouOtherText}"
                        maxLength="60"
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>
        
        <t:radiobuttons id="carerSomewhereElseWhereCaree" 
                        name="carerSomewhereElseWhereCaree" 
                        optionValues="on holiday|at home|somewhere else"                        
                        optionLabels="on holiday|at home|somewhere else"
                        value="${carerSomewhereElseWhereCaree}"
                        label="Where was the person you care for during the break?"
                        errors="${validationErrors}" 
                        hintBefore="During this time, they were:" 
        />
        
        <t:hiddenPanel id="carerSomewhereElseWhereCareeWrap" triggerId="carerSomewhereElseWhereCaree" triggerValue="somewhere else">
            <t:textarea id="carerSomewhereElseWhereCareeOtherText"
                        name="carerSomewhereElseWhereCareeOtherText"
                        value="${carerSomewhereElseWhereCareeOtherText}"
                        maxLength="60"
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
