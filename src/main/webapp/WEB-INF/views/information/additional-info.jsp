<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="anythingElse" />

        <t:hiddenPanel id="anythingElseWrap" triggerId="anythingElse" triggerValue="yes">
            <t:textarea name="anythingElseText" showRemainingChars="true" />
        </t:hiddenPanel>

        <t:yesnofield name="welshCommunication" />

    </t:pageContent>

</t:mainPage>    
