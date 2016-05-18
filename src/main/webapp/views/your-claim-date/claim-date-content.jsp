<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<article class="column-three-quarters main-content">
    <nav class="back-nav" role="navigation">
        <a id="topBackButton" name="topBackButton" href="/about-you/your-details">Back</a>
    </nav>

    <h1 class="form-title heading-large">
        <span class="section-progress"> Section 2 of 11 </span> When do you want Carer's Allowance to start?
    </h1>

    <p>
        Most claims can be backdated 3 months. You may be able to 
        <a rel='external' 
           href='/claim-help#1'
           target='_blank'
           onmousedown="trackEvent(&#x27;/your-claim-date/claim-date&#x27;,&#x27;Claim Notes - Backdating Claim Date&#x27;);"
           onkeydown="trackEvent(&#x27;/your-claim-date/claim-date&#x27;,&#x27;Claim Notes - Backdating Claim Date&#x27;);"
        >backdate it further</a> 
        if the person you care for was awarded their qualifying benefit in the last 3 months.
    </p>

    <form action="/your-claim-date/claim-date" method="POST" role="form">
        <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />
        <div class="form-elements">
            <ul>
                <li class="form-group ">
                    <fieldset class="question-group">
                        <legend id="dateOfClaim_questionLabel" class="form-label-bold"> Claim date </legend>
                        <p class="form-hint" id="dateOfClaim_defaultDateContextualHelp">
                            This is when you want to claim Carer's Allowance from, eg 16 2 2016.
                        </p>
                        
                        <!-- datePlaceholder -->
                        <ul class="form-date" id="dateOfClaim">
                            <li class="form-group">
                                <label for="dateOfClaim_day">Day</label>
                                <input type="tel"
                                       class="form-control"
                                       id="dateOfClaim_day"
                                       name="dateOfClaim.day"
                                       title="Must be numbers only"
                                       value="16" 
                                       maxLength="2"
                                       autocomplete="off"
                                >
                            </li>
                            
                            <li class="form-group month">
                                <label for="dateOfClaim_month">Month</label>
                                <input type="tel"
                                       class="form-control"
                                       id="dateOfClaim_month"
                                       name="dateOfClaim.month"
                                       title="Must be numbers only"
                                       value="5" 
                                       maxLength="2"
                                       autocomplete="off">
                            </li>
                            
                            <li class="form-group form-group-year">
                                <label for="dateOfClaim_year">Year</label>
                                <input type="tel"
                                       class="form-control"
                                       id="dateOfClaim_year"
                                       name="dateOfClaim.year"
                                       title="Must be numbers only"
                                       value="2016" 
                                       maxLength="4"
                                       autocomplete="off"
                                >
                            </li>
                        </ul>
                    </fieldset> 
                    <!-- dateClaimDatePlaceholder -->

                </li>

                <div id="claimDateWarning" class="prompt breaks-prompt validation-summary">
                    <p>You can&#x27;t claim Carer&#x27;s Allowance more than 3 months in advance.</p>
                </div>
                
                <li class="form-group ">
                    <fieldset class="question-group">
                        <legend id="beforeClaimCaring_answer_questionLabel" class="form-label-bold ">
                            Were you caring for the person for more than 35 hours a week before this date? 
                        </legend>

                        <ul class="inline " id="beforeClaimCaring_answer">

                            <li>
                                <label class="block-label" for="beforeClaimCaring_answer_yes" onmousedown=""> 
                                    <input type="radio"
                                           id="beforeClaimCaring_answer_yes"
                                           name="beforeClaimCaring.answer"
                                           onmousedown=""
                                           value="yes" checked 
                                    />
                                    <span>Yes</span>
                                </label>
                            </li>

                            <li>
                                <label class="block-label" for="beforeClaimCaring_answer_no" onmousedown=""> 
                                    <input type="radio"
                                           id="beforeClaimCaring_answer_no"
                                           name="beforeClaimCaring.answer"
                                           onmousedown=""
                                           value="no" 
                                    /> 
                                    <span>No</span>
                                </label>
                            </li>
                        </ul>
                    </fieldset>
                </li>

                <li id="careStartDateWrap" class="form-group">
                    <ul class="extra-group">
                        <li class="form-group ">
                            <fieldset class="question-group">
                                <legend id="beforeClaimCaring_date_questionLabel" class="form-label-bold">
                                    When did you begin caring? 
                                </legend>

                                <p class="form-hint" id="beforeClaimCaring_date_defaultDateContextualHelp">
                                    For example, 16 5 2015
                                </p>
                                
                                <!-- datePlaceholder -->
                                <ul class="form-date" id="beforeClaimCaring_date">
                                    <li class="form-group">
                                        <label for="beforeClaimCaring_date_day">Day</label>
                                        <input type="tel"
                                               class="form-control"
                                               id="beforeClaimCaring_date_day"
                                               name="beforeClaimCaring.date.day"
                                               title="Must be numbers only"
                                               value="16"
                                               maxLength="2"
                                               autocomplete="off">
                                    </li>
                                    <li class="form-group month">
                                        <label for="beforeClaimCaring_date_month">Month</label>
                                        <input type="tel"
                                               class="form-control"
                                               id="beforeClaimCaring_date_month"
                                               name="beforeClaimCaring.date.month"
                                               title="Must be numbers only"
                                               value="2"
                                               maxLength="2"
                                               autocomplete="off">
                                    </li>
                                    <li class="form-group form-group-year">
                                        <label for="beforeClaimCaring_date_year">Year</label>
                                        <input type="tel"
                                               class="form-control"
                                               id="beforeClaimCaring_date_year"
                                               name="beforeClaimCaring.date.year"
                                               title="Must be numbers only"
                                               value="2016"
                                               maxLength="4"
                                               autocomplete="off"
                                        >
                                    </li>
                                </ul>
                            </fieldset>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <nav class="form-steps" role="navigation">
            <ul>
                <li>
                    <button type="submit" id="next" name="action" value="next" class="button">Next</button>
                </li>

                <li>
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
                </li>

                <li>
                    <a id="bottomBackButton" name="bottomBackButton" href="/about-you/your-details">Back</a>
                </li>
            </ul>
        </nav>
    </form>

    <script type="text/javascript" src="/assets/javascript/s_claim_date/g_claimDate.js"></script>

    <script type="text/javascript">
        $(function() {
            window.initEvents("beforeClaimCaring_answer_yes",
                              "beforeClaimCaring_answer_no",
                              "beforeClaimCaring_date_day",
                              "beforeClaimCaring_date_month",
                              "beforeClaimCaring_date_year");

            window.initDateWarning("claimDateWarning",
                                   "dateOfClaim_day",
                                   "dateOfClaim_month",
                                   "dateOfClaim_year",
                                   "You can&#x27;t claim Carer&#x27;s Allowance more than 3 months in advance.",
                                   false);
        });

    </script>
</article>