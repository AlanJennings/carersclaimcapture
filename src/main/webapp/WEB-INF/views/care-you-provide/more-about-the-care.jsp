<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <t:yesnofield name="spent35HoursCaring" />

        <t:yesnofield name="otherCarer" />

        <t:hiddenPanel id="otherCarerWrap" triggerId="otherCarer" triggerValue="yes">
            <t:yesnofield name="otherCarerUc" />

            <t:hiddenPanel id="otherCarerUcWrap" triggerId="otherCarerUc" triggerValue="yes">
                <t:textarea name="otherCarerUcDetails" showRemainingChars="true" />
            </t:hiddenPanel>
        </t:hiddenPanel>
    </t:pageContent>
</t:mainPage>    
