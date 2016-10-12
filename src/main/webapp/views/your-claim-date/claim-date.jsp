<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:htmlsection name="claimDateIntro">
            <p>
                <t:message code="claimDateIntro.line1" />
                <a rel="external" href="/claim-help#1" target="_blank"><t:message code="claimDateIntro.line2" /></a> 
                <t:message code="claimDateIntro.line3" />
            </p>
        </t:htmlsection>

        <t:datefield name="dateOfClaim" />

        <%-- TODO can we replace the warning with the warning component? --%>
        <t:pre>
            <div id="claimDateWarning" class="prompt breaks-prompt validation-summary">
                <p><t:message code="claimDateWarning.line1" /></p>
            </div>
        </t:pre>

        <t:yesnofield name="beforeClaimCaring" />

        <t:hiddenPanel id="careStartDateWrap" triggerId="beforeClaimCaring" triggerValue="yes" >
            <t:datefield name="beforeClaimCaringDate" />
        </t:hiddenPanel>
    </t:pageContent>

    <script type="text/javascript" src="<c:url value='/assets/javascript/s_claim_date/g_claimDate.js' />"></script>

    <script type="text/javascript">
        $(function() {
            window.initDateWarning("claimDateWarning",
                                   "dateOfClaim_day",
                                   "dateOfClaim_month",
                                   "dateOfClaim_year",
                                   "You can't claim Carer's Allowance more than 3 months in advance.",
                                   false);
        });

    </script>

</t:mainPage>