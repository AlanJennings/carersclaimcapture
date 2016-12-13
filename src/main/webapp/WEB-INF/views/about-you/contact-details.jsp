<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <!-- validationErrors = '${validationErrors}' -->
        <t:address name="carerAddress" />
        <t:textedit name="carerPostcode" additionalClasses="postcode" />
        <t:textedit name="carerHowWeContactYou" />
        <t:checkbox name="carerContactYouByTextPhone" additionalClasses="checkbox-single" outerClass="form-group checkbox-single" blockLabel="false" />
        <t:yesnofield name="carerWantsEmailContact"/>
        
        <t:hiddenPanel id="carerWantsEmailContactWrap" triggerId="carerWantsEmailContact" triggerValue="yes">
            <t:textedit name="carerMail" /> 
            <t:textedit name="carerMailConfirmation" />
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>

