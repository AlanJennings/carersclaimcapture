<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <t:yesnofield name="empAdditionalInfo" />

        <t:hiddenPanel id="empAdditionalInfoWrap" triggerId="empAdditionalInfo" triggerValue="yes">
            <t:textarea name="empAdditionalInfoText" maxLength="3000" showRemainingChars="true" labelKey="blankLabel"/>
        </t:hiddenPanel>
    </t:pageContent>

</t:mainPage>    
