<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="break-in-hospital" backLink="${previousPage}">

        <t:htmlsection name="breakInHospitalIntro">
            <p>You told us that you or the person you care for have been in hospital.  You could still be paid Carer's Allowance for this time</p>
        </t:htmlsection>

        <t:radiobuttons name="whoInHospital" optionValues="Carer|Caree" />

        <t:hiddenPanel id="whoInHospitalWrapCarer" triggerId="whoInHospital" triggerValue="Carer">

            <t:datefield name="carerHospitalStartDate" />
            <t:yesnofield name="carerHospitalStayEnded" />

            <t:hiddenPanel id="carerHospitalStayEndedWrap" triggerId="carerHospitalStayEnded" triggerValue="You">

                <t:datefield name="carerHospitalEndDate" />
                <t:radiobuttons name="carerInHospitalCareeLocation" optionValues="In hospital|In respite care|Somewhere else" optionLabelKeys="hospital|respite|elsewhere" />

                <t:hiddenPanel id="carerInHospitalCareeLocationWrap" triggerId="carerInHospitalCareeLocation" triggerValue="Somewhere else">
                    <t:textedit name="carerInHospitalCareeLocationText" maxLength="30" />
                </t:hiddenPanel>

            </t:hiddenPanel>

        </t:hiddenPanel>

        <t:hiddenPanel id="whoInHospitalWrapCaree" triggerId="whoInHospital" triggerValue="Caree">

            <t:datefield name="careeHospitalStartDate" />
            <t:yesnofield name="careeHospitalStayEnded" />

            <t:hiddenPanel id="careeHospitalStayEnded" triggerId="careeHospitalStayEnded" triggerValue="You">

                <t:datefield name="careeHospitalEndDate" />
                <t:yesnofield name="careeHospitalCarerStillCaring" />

            </t:hiddenPanel>
            
        </t:hiddenPanel>
       
        <t:yesnofield name="weeksNotCaring" />
        
    </t:pageContent>

</t:mainPage>    
