<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.your-details" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.your-details" backLink="${previousPage}">
    
        <t:textedit name="carerTitle" maxLength="20" />
        <t:textedit name="carerFirstName" maxLength="17" />
        <t:textedit name="carerMiddleName" maxLength="17" />
        <t:textedit name="carerSurname" maxLength="35" />
        
        <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:textedit name="carerNationalInsuranceNumber" maxLength="19" additionalClasses="ni-number" />
        
        <t:datefield name="carerDateOfBirth" />

    </t:pageContent>

</t:mainPage>    
