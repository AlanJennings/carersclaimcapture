<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.break-in-hospital" backLink="${previousPage}">

        <t:htmlsection name="breakInHospitalIntro">
            <p>You told us that you or the person you care for have been in hospital.  You could still be paid Carer's Allowance for this time</p>
        </t:htmlsection>

        <t:radiobuttons name="hospitalBreakWhoInHospital" optionValues="Carer|Caree" />

        <t:hiddenPanel id="hospitalBreakWhoInHospitalWrapCarer" triggerId="hospitalBreakWhoInHospital" triggerValue="Carer">

            <t:datefield name="hospitalBreakCarerHospitalStartDate" />
            <t:yesnofield name="hospitalBreakCarerHospitalStayEnded" />

            <t:hiddenPanel id="hospitalBreakCarerHospitalStayEndedWrap" triggerId="hospitalBreakCarerHospitalStayEnded" triggerValue="yes">

                <t:datefield name="hospitalBreakCarerHospitalEndDate" />
                <t:radiobuttons name="hospitalBreakCarerInHospitalCareeLocation" 
                                optionValues="In hospital|In respite care|Somewhere else" 
                                optionLabelKeys="hospital|respite|elsewhere" 
                                labelKeyArgs="${careeFirstName} ${careeSurname}"/>

                <t:hiddenPanel id="hospitalBreakCarerInHospitalCareeLocationWrap" triggerId="hospitalBreakCarerInHospitalCareeLocation" triggerValue="Somewhere else">
                    <t:textedit name="hospitalBreakCarerInHospitalCareeLocationText" maxLength="30" />
                </t:hiddenPanel>

            </t:hiddenPanel>

        </t:hiddenPanel>

        <t:hiddenPanel id="hospitalBreakWhoInHospitalWrapCaree" triggerId="hospitalBreakWhoInHospital" triggerValue="Caree">

            <t:datefield name="hospitalBreakCareeHospitalStartDate" />
            <t:yesnofield name="hospitalBreakCareeHospitalStayEnded" />

            <t:hiddenPanel id="hospitalBreakCareeHospitalStayEndedWrap" triggerId="hospitalBreakCareeHospitalStayEnded" triggerValue="yes">

                <t:datefield name="hospitalBreakCareeHospitalEndDate" />
                <t:yesnofield name="hospitalBreakCareeHospitalCarerStillCaring" />

            </t:hiddenPanel>
            
        </t:hiddenPanel>
       
        <t:yesnofield name="hospitalBreakWeeksNotCaring" labelKeyArgs="${careeFirstName} ${careeSurname}" />
        
    </t:pageContent>

</t:mainPage>    
