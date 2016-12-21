<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}">
        <t:radiobuttons name="countryAnswer" optionValues="NI|ESW|AO" />
        <t:hiddenWarning id="answerNoMessageWrap" triggerId="benefitsAnswer" triggerValue="NONE">
            <p><t:message code="countryAnswer.warning.message" /></p>
        </t:hiddenWarning>
    </t:pageContent>
    <t:googleAnalyticsTiming tracktype="STARTELIGIBLE"/>
</t:mainPage>


