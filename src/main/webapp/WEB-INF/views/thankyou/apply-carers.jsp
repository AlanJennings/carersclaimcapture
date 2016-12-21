<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent nextLink="none">
        <section id="app-complete" class="app-complete">
            <h1 class="form-title heading-large compound"><t:message code="finish-statement.line1" /></h1>
            <p class="nino-title"><t:message code="finish-statement.line2" /></p>
        </section>

        <p class="hide-desktops"><t:message code="finish-statement.line3" /></p>

        <div class="finish-info">
            <h2 class="heading-medium"><t:message code="finish-statement.line4" /></h2>
                <p><t:message code="finish-statement.line5" /></p>
        </div>

        <br>

        <p>
            <a rel="external" href="https://www.gov.uk/carers-allowance-report-change" target="_blank">
                <t:message code="finish-statement.line6" />
            </a>
        </p>

        <div class="finish-button">
            <a href="https://www.gov.uk/done/apply-carers-allowance"
               rel="external"
               target="_blank"
               class="secondary"
               onmousedown="trackEvent('/thankyou/apply-carers','Feedback');"
               onkeydown="trackEvent('/thankyou/apply-carers','Feedback');"
            ><t:message code="finish-statement.line7" /></a>
            <t:message code="finish-statement.line8" />
        </div>
        <div style="height: 50px;" ></div>
    </t:pageContent>
    <t:googleAnalyticsSendEvent action="Assisted-Decision" label="TODO-ASSISTED-DECISION" />
    <t:googleAnalyticsTiming tracktype="ENDCLAIM" />
</t:mainPage>
