<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.break-in-respite-care" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.break-in-respite-care" backLink="${previousPage}">
        <input type="hidden" name="break_id" value="${break_id}" >
        <input type="hidden" name="breakInCareType" value="respite" >
        
        <t:htmlsection name="breakInRespiteIntro">
            <p><t:message code="breakInRespiteIntro.text"/></p>
        </t:htmlsection>

        <t:radiobuttons name="respiteBreakWhoInRespite" optionValues="Carer|Caree" />
        
        <t:hiddenPanel id="respiteBreakWhoInRespiteWrapCarer" triggerId="respiteBreakWhoInRespite" triggerValue="Carer">

            <t:datefield name="respiteBreakCarerRespiteStartDate" />
            <t:yesnofield name="respiteBreakCarerRespiteStayEnded" />

            <t:hiddenPanel id="respiteBreakCarerRespiteStayEndedWrap" triggerId="respiteBreakCarerRespiteStayEnded" triggerValue="yes">
                <t:datefield name="respiteBreakCarerRespiteEndDate" />
                <t:yesnofield name="respiteBreakCarerRespiteStayMedicalCare" />
            </t:hiddenPanel>

        </t:hiddenPanel>

        <t:hiddenPanel id="respiteBreakWhoInRespiteWrapCaree" triggerId="respiteBreakWhoInRespite" triggerValue="Caree">

            <t:datefield name="respiteBreakCareeRespiteStartDate" />
            <t:yesnofield name="respiteBreakCareeRespiteStayEnded" />

            <t:hiddenPanel id="respiteBreakCareeRespiteStayEndedWrap" triggerId="respiteBreakCareeRespiteStayEnded" triggerValue="yes">
                <t:datefield name="respiteBreakCareeRespiteEndDate" />
                <t:yesnofield name="respiteBreakCareeRespiteStayMedicalCare" />
                <t:yesnofield name="respiteBreakCareeRespiteCarerStillCaring" />
            </t:hiddenPanel>

        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>
