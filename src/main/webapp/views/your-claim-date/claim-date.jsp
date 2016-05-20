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
    
        <p>
            Most claims can be backdated 3 months. You may be able to 
            <a rel='external' 
               href='/claim-help#1'
               target='_blank'
               onmousedown="trackEvent('${currentPage}','Claim Notes - Backdating Claim Date');"
               onkeydown="trackEvent('${currentPage}','Claim Notes - Backdating Claim Date');"
            >backdate it further</a> 
            if the person you care for was awarded their qualifying benefit in the last 3 months.
        </p>
    
        <form action="<c:url value='${currentPage}' />" method="POST" role="form">
            <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />
            <jsp:include page="../validationSummary.jsp" />
            <fieldset class="form-elements">
                <ul>
                    
                    <c:if test="${errors.hasError('dateOfClaim')}" >
                        <c:set var="dateOfClaim_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group  <c:out value='${dateOfClaim_AdditionalClass}'/>">
                        <c:if test="${errors.hasError('dateOfClaim')}" >
                            <p class="validation-message">${errors.getErrorMessage('dateOfClaim')}</p>
                        </c:if>
                        <fieldset class="question-group">
                            <legend id="dateOfClaim_questionLabel" class="form-label-bold"> Claim date </legend>
                            <p class="form-hint" id="dateOfClaim_defaultDateContextualHelp">
                                This is when you want to claim Carer's Allowance from, eg 16 2 2016.
                            </p>
                            
                            <ul class="form-date" id="dateOfClaim">
                                <li class="form-group day">
                                    <label for="dateOfClaim_day">Day</label>
                                    <input type="tel"                   <%-- TODO pretty sure type=tel is not the correct choice --%>
                                           class="form-control"
                                           id="dateOfClaim_day"
                                           name="dateOfClaim_day"
                                           title="Must be numbers only"
                                           value="${dateOfClaim_day}" 
                                           maxLength="2"
                                           autocomplete="off"
                                    >
                                </li>
                                
                                <li class="form-group month">
                                    <label for="dateOfClaim_month">Month</label>
                                    <input type="tel"
                                           class="form-control"
                                           id="dateOfClaim_month"
                                           name="dateOfClaim_month"
                                           title="Must be numbers only"
                                           value="${dateOfClaim_month}" 
                                           maxLength="2"
                                           autocomplete="off">
                                </li>
                                
                                <li class="form-group form-group-year">
                                    <label for="dateOfClaim_year">Year</label>
                                    <input type="tel"
                                           id="dateOfClaim_year"
                                           class="form-control"
                                           name="dateOfClaim_year"
                                           title="Must be numbers only"
                                           value="${dateOfClaim_year}" 
                                           maxLength="4"
                                           autocomplete="off"
                                    >
                                </li>
                            </ul>
                        </fieldset> 
    
                    </li>
    
                    <div id="claimDateWarning" class="prompt breaks-prompt validation-summary">
                        <p>You can't claim Carer's Allowance more than 3 months in advance.</p>
                    </div>
                    
                    <c:if test="${errors.hasError('beforeClaimCaring')}" >
                        <c:set var="beforeClaimCaring_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group <c:out value='${beforeClaimCaring_AdditionalClass}'/>">
                        <c:if test="${errors.hasError('beforeClaimCaring')}" >
                            <p class="validation-message">${errors.getErrorMessage('beforeClaimCaring')}</p>
                        </c:if>

                        <fieldset class="question-group">
                            <legend id="beforeClaimCaring_answer_questionLabel" class="form-label-bold ">
                                Were you caring for the person for more than 35 hours a week before this date? 
                            </legend>
    
                            <ul class="inline " id="beforeClaimCaring_answer">
                                <li>
                                    <label class="block-label" for="beforeClaimCaring_answer_yes" onmousedown=""> 
                                        <input type="radio"
                                               id="beforeClaimCaring_answer_yes"
                                               name="beforeClaimCaring"
                                               value="yes" 
                                               <c:if test="${beforeClaimCaring=='yes'}">checked</c:if>
                                               onmousedown=""
                                        />
                                        <span>Yes</span>
                                    </label>
                                </li>
    
                                <li>
                                    <label class="block-label" for="beforeClaimCaring_answer_no" onmousedown=""> 
                                        <input type="radio"
                                               id="beforeClaimCaring_answer_no"
                                               name="beforeClaimCaring"
                                               value="no" 
                                               <c:if test="${beforeClaimCaring=='no'}">checked</c:if>
                                               onmousedown=""
                                        /> 
                                        <span>No</span>
                                    </label>
                                </li>
                            </ul>
                        </fieldset>
                    </li>
    
                    <li id="careStartDateWrap" class="form-group">
                        <ul class="extra-group">
                            <c:if test="${errors.hasError('beforeClaimCaringDate')}" >
                                <c:set var="beforeClaimCaring_date_AdditionalClass" value="validation-error" />
                            </c:if>
                            <li class="form-group <c:out value='${beforeClaimCaring_date_AdditionalClass}'/>">
                                <c:if test="${errors.hasError('beforeClaimCaringDate')}" >
                                    <p class="validation-message">${errors.getErrorMessage('beforeClaimCaringDate')}</p>
                                </c:if>
                                
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
                                                   name="beforeClaimCaringDate_day"
                                                   title="Must be numbers only"
                                                   value="${beforeClaimCaringDate_day}"
                                                   maxLength="2"
                                                   autocomplete="off">
                                        </li>
                                        <li class="form-group month">
                                            <label for="beforeClaimCaring_date_month">Month</label>
                                            <input type="tel"
                                                   class="form-control"
                                                   id="beforeClaimCaring_date_month"
                                                   name="beforeClaimCaringDate_month"
                                                   title="Must be numbers only"
                                                   value="${beforeClaimCaringDate_month}"
                                                   maxLength="2"
                                                   autocomplete="off">
                                        </li>
                                        <li class="form-group form-group-year">
                                            <label for="beforeClaimCaring_date_year">Year</label>
                                            <input type="tel"
                                                   class="form-control"
                                                   id="beforeClaimCaring_date_year"
                                                   name="beforeClaimCaringDate_year"
                                                   title="Must be numbers only"
                                                   value="${beforeClaimCaringDate_year}"
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
            </fieldset>
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
                        <a id="bottomBackButton" name="bottomBackButton" href="<c:url value='${previousPage}' />">Back</a>
                    </li>
                </ul>
            </nav>
        </form>

        <script type="text/javascript" src="<c:url value='/assets/javascript/s_claim_date/g_claimDate.js' />"></script>
    
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
                                       "You can't claim Carer's Allowance more than 3 months in advance.",
                                       false);
            });
    
        </script>
    </article>
</t:mainPage>