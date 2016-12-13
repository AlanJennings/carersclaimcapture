<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
    
        <t:textedit name="carerTitle" />
        <t:textedit name="carerFirstName" />
        <t:textedit name="carerMiddleName" />
        <t:textedit name="carerSurname" />
        
        <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
        <t:textedit name="carerNationalInsuranceNumber" additionalClasses="ni-number" />
        
        <t:datefield name="carerDateOfBirth" />

    </t:pageContent>

</t:mainPage>    
