<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="When do you want Carer's Allowance to start?" section="Section 2 of 11" backLink="${previousPage}">
        <t:hmtlsection>
            <p>
                Most claims can be backdated 3 months. You may be able to 
                <a rel="external" 
                   href="/claim-help#1"
                   target="_blank"
                   onmousedown="trackEvent(&#39;${currentPage}&#39;,&#39;Claim Notes - Backdating Claim Date&#39;);"
                   onkeydown="trackEvent(&#39;${currentPage}&#39;,&#39;Claim Notes - Backdating Claim Date&#39;);"
                >backdate it further</a> 
                if the person you care for was awarded their qualifying benefit in the last 3 months.
            </p>
        </t:hmtlsection>
        
        <t:datefield id="dateOfClaim" 
                     nameDay="dateOfClaim_day" 
                     nameMonth="dateOfClaim_month" 
                     nameYear="dateOfClaim_year" 
                     valueDay="${dateOfClaim_day}" 
                     valueMonth="${dateOfClaim_month}" 
                     valueYear="${dateOfClaim_year}" 
                     label="Claim date" 
                     errors="${validationErrors}" 
                     hintBeforeId="dateOfClaim_defaultDateContextualHelp"
                     hintBefore="This is when you want to claim Carer&rsquo;s Allowance from, eg 16 2 2016."
        />

        <t:pre>
            <div id="claimDateWarning" class="prompt breaks-prompt validation-summary">
                <p>You can't claim Carer's Allowance more than 3 months in advance.</p>
            </div>
        </t:pre>

        <t:yesnofield id="beforeClaimCaring" 
                      name="beforeClaimCaring" 
                      value="${beforeClaimCaring}"
                      label="Were you caring for the person for more than 35 hours a week before this date?" 
                      errors="${validationErrors}" 
        />

        <t:hiddenPanel id="careStartDateWrap" triggerId="beforeClaimCaring" triggerValue="yes" >
            <t:datefield id="beforeClaimCaringDate" 
                         nameDay="beforeClaimCaringDate_day" 
                         nameMonth="beforeClaimCaringDate_month" 
                         nameYear="beforeClaimCaringDate_year" 
                         valueDay="${beforeClaimCaringDate_day}" 
                         valueMonth="${beforeClaimCaringDate_month}" 
                         valueYear="${beforeClaimCaringDate_year}" 
                         label="When did you begin caring?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 16 5 2015"
                         hintBeforeId="beforeClaimCaring_date_defaultDateContextualHelp"
            />
        </t:hiddenPanel>
    </t:pageContent>

    <script type="text/javascript">
        $(document).ready(function(){
            $("#save").click(function(){
                var saveurl=$(this).attr("href");
                var saveurl=$(this).attr("href");
                $("form").attr( "action", saveurl );
                $("form").submit()
            });
        });
    </script>

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