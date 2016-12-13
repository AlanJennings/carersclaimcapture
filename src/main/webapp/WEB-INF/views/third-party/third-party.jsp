<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}"> 
        
        <t:radiobuttons name="thirdParty" optionValues="yes|no" />

        <t:hiddenPanel id="thirdPartyWrap" triggerId="thirdParty" triggerValue="no">
            <t:textedit name="nameAndOrganisation" />
        </t:hiddenPanel>
    
    </t:pageContent>
    <t:googleAnalyticsTiming tracktype="STARTCLAIM"/>
</t:mainPage>    
