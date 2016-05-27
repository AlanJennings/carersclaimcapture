<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- <c:out value="${errors}"/> -->

<t:mainPage>
    <article class="column-three-quarters main-content">
        <nav class="back-nav" role="navigation">
            <a id="topBackButton" name="topBackButton" href="<c:url value='${previousPage}' />">Back</a>
        </nav>
    
        <h1 class="form-title heading-large">
            <span class="section-progress"> Section 2 of 11 </span> 
            When do you want Carer's Allowance to start?
        </h1>

        <form action="<c:url value='${currentPage}' />" method="POST" role="form" style="margin-bottom: 30px; margin-top: -30px;">
            <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />
            <jsp:include page="../validationSummary.jsp" />
            <fieldset class="form-elements">
                <ul>
                    
                    <t:hmtlsection value='<p>
                                              Most claims can be backdated 3 months. You may be able to 
                                              <a rel="external" 
                                                 href="/claim-help#1"
                                                 target="_blank"
                                                 onmousedown="trackEvent(&#39;${currentPage}&#39;,&#39;Claim Notes - Backdating Claim Date&#39;);"
                                                 onkeydown="trackEvent(&#39;${currentPage}&#39;,&#39;Claim Notes - Backdating Claim Date&#39;);"
                                              >backdate it further</a> 
                                              if the person you care for was awarded their qualifying benefit in the last 3 months.
                                          </p>' />
                    
                    <t:datefield id="dateOfClaim" 
                                 nameDay="dateOfClaim_day" 
                                 nameMonth="dateOfClaim_month" 
                                 nameYear="dateOfClaim_year" 
                                 valueDay="${dateOfClaim_day}" 
                                 valueMonth="${dateOfClaim_month}" 
                                 valueYear="${dateOfClaim_year}" 
                                 label="Claim date" 
                                 hasError="${errors.hasError('dateOfClaim')}" 
                                 errorMessage="${errors.getErrorMessage('dateOfClaim')}" 
                                 hintBefore='<p class="form-hint" id="dateOfClaim_defaultDateContextualHelp">
                                                 This is when you want to claim Carer&rsquo;s Allowance from, eg 16 2 2016.
                                             </p>'
                    />

                    <t:hmtlsection value='<div id="claimDateWarning" class="prompt breaks-prompt validation-summary">
                                              <p>You can&rsquo;t claim Carer&rsquo;s Allowance more than 3 months in advance.</p>
                                          </div>' />

                    <t:yesnofield id="beforeClaimCaring" 
                                  name="beforeClaimCaring" 
                                  value="${beforeClaimCaring}"
                                  label="Were you caring for the person for more than 35 hours a week before this date?" 
                                  hasError="${errors.hasError('beforeClaimCaring')}" 
                                  errorMessage="${errors.getErrorMessage('beforeClaimCaring')}" 
                    />

                    <li id="careStartDateWrap" class="form-group">
                        <ul class="extra-group">
                            <t:datefield id="beforeClaimCaringDate" 
                                         nameDay="beforeClaimCaringDate_day" 
                                         nameMonth="beforeClaimCaringDate_month" 
                                         nameYear="beforeClaimCaringDate_year" 
                                         valueDay="${beforeClaimCaringDate_day}" 
                                         valueMonth="${beforeClaimCaringDate_month}" 
                                         valueYear="${beforeClaimCaringDate_year}" 
                                         label="When did you begin caring?" 
                                         hasError="${errors.hasError('beforeClaimCaringDate')}" 
                                         errorMessage="${errors.getErrorMessage('beforeClaimCaringDate')}" 
                                         hintBefore='<p class="form-hint" id="beforeClaimCaring_date_defaultDateContextualHelp">For example, 16 5 2015</p>'
                            />
                        </ul>
                    </li>

                </ul>
            </fieldset>
            <nav class="form-steps" role="navigation">
                <ul>
                    <li>
                        <button type="submit" id="next" name="action" value="next" class="button">Next</button>
                    </li>
                </ul>
            </nav>
        </form>
        
        <nav class="back-nav" role="navigation">
            <a id="bottomBackButton" name="bottomBackButton" href="<c:url value='${previousPage}' />">Back</a>
        </nav>

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
                window.initEvents("beforeClaimCaring_yes",
                                  "beforeClaimCaring_no",
                                  "beforeClaimCaring_date_day",
                                  "beforeClaimCaring_date_month",
                                  "beforeClaimCaring_date_year");
    
                window.initDateWarning("claimDateWarning",
                                       "dateOfClaim_day",
                                       "dateOfClaim_month",
                                       "dateOfClaim_year",
                                       "You can't claim Carer's Allowance more than 3 months in advance.",
                                       false);
            });
    
        </script>
    </article>
</t:mainPage>