<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.break-in-respite-care" backLink="${previousPage}">

        <t:htmlsection name="breakInRespiteIntro">
            <p><t:message code="breakInRespiteIntro.text"/></p>
        </t:htmlsection>

        <t:radiobuttons name="respiteBreakWhoInRespite" optionValues="Carer|Caree" />

        <t:hiddenPanel id="respiteBreakWhoInRespiteWrapCarer" triggerId="respiteBreakWhoInRespite" triggerValue="Carer">

            <t:datefield name="respiteBreakCarerRespiteStartDate" />
            <t:yesnofield name="respiteBreakCarerRespiteStayEnded" />

            <t:hiddenPanel id="respiteBreakCarerRespiteStayEnded" triggerId="respiteBreakCarerRespiteStayEnded" triggerValue="You">
                <t:datefield name="respiteBreakCarerRespiteEndDate" />
                <t:yesnofield name="respiteBreakCarerRespiteStayMedicalCare" />
            </t:hiddenPanel>

        </t:hiddenPanel>

        <t:hiddenPanel id="respiteBreakWhoInRespiteWrapCaree" triggerId="respiteBreakWhoInRespite" triggerValue="Caree">

            <t:datefield name="respiteBreakCareeRespiteStartDate" />
            <t:yesnofield name="respiteBreakCareeRespiteStayEnded" />

            <t:hiddenPanel id="respiteBreakCareeRespiteStayEnded" triggerId="respiteBreakCareeRespiteStayEnded" triggerValue="You">
                <t:datefield name="respiteBreakCareeRespiteEndDate" />
                <t:yesnofield name="respiteBreakCareeRespiteStayMedicalCare" />
                <t:yesnofield name="respiteBreakCareeRespiteCarerStillCaring" />
            </t:hiddenPanel>

        </t:hiddenPanel>

        <t:yesnofield name="respiteBreakWeeksNotCaring" />

    </t:pageContent>

</t:mainPage>
