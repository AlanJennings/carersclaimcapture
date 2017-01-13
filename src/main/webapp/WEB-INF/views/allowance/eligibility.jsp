<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}?changing=true">
    
        <t:yesnofield name="over35HoursAWeek" />
        
        <t:hiddenWarning id="warningover35HoursAWeek_answer" triggerId="over35HoursAWeek" triggerValue="no">
            <p><t:message code="over35HoursAWeek.warning.message" /></p>
        </t:hiddenWarning>

        <t:yesnofield name="over16YearsOld" />
        
        <t:hiddenWarning id="warningover16_answer" triggerId="over16YearsOld" triggerValue="no">
            <t:message code="over16YearsOld.warning.message" /></p>
        </t:hiddenWarning>

        <t:radiobuttons name="originCountry" optionValues="GB|NI|OTHER" />
        
        <t:hiddenWarning id="warningOriginCountry_answer" triggerId="originCountry" triggerValue="OTHER">
            <p><t:message code="originCountry.warning.message" /></p>
        </t:hiddenWarning>
        
    </t:pageContent>
    
</t:mainPage>