<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <!-- validationErrors = '${validationErrors}' -->
        <t:address name="carerAddress" maxlength="35"/>
        <t:textedit name="carerPostcode" maxLength="10" />
        <t:textedit name="carerHowWeContactYou" maxLength="20" />
        <t:checkbox name="carerContactYouByTextPhone" additionalClasses="checkbox-single" outerClass="form-group checkbox-single" blockLabel="false" />
        <t:yesnofield name="carerWantsEmailContact"/>
        
        <t:hiddenPanel id="carerWantsEmailContactWrap" triggerId="carerWantsEmailContact" triggerValue="yes">
            <t:textedit name="carerMail" maxLength="254"  /> 
            <t:textedit name="carerMailConfirmation" maxLength="254" /> 
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
