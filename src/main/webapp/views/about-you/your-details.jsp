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
            <span class="section-progress"> Section 3 of 11 </span> About you - the carer
        </h1>
        
        <form action="<c:url value='${currentPage}' />" method="POST" role="form">
            <jsp:include page="../validationSummary.jsp" />
            <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />
            <fieldset class="form-elements" data-journey="carers-allowance:page:about-you">
                <ul>
                    <li class="form-group ">
                        <label id="title_questionLabel" class="form-label-bold" for="title"> Title </label> 
                        <input type="text" class="form-control " id="title" name="title" value="${title}" maxLength="20" autocomplete="off">
                    </li>
                    
                    <li class="form-group ">
                        <label id="firstName_questionLabel" class="form-label-bold" for="firstName"> First name </label> 
                        <input type="text" class="form-control " id="firstName" name="firstName" value="${firstName}" maxLength="17" autocomplete="off">
                    </li>
    
                    <li class="form-group ">
                        <label id="middleName_questionLabel" class="form-label-bold" for="middleName"> Middle name(s) </label> 
                        <input type="text" class="form-control " id="middleName" name="middleName" value="${middleName}" maxLength="17" autocomplete="off">
                    </li>
    
                    <li class="form-group ">
                        <label id="surname_questionLabel" class="form-label-bold" for="surname"> Last name </label> 
                        <input type="text" class="form-control " id="surname" name="surname" value="${surname}" maxLength="35" autocomplete="off">
                    </li>
    
                    <li class="form-group ">
                        <label id="nationalInsuranceNumber" class="form-label-bold" for="nationalInsuranceNumber_nino"> National Insurance number </label>
                        <p class="form-hint">For example, VO123456D</p> 
                        <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
    
                        <input type="text" 
                               class="form-control ni-number" 
                               id="nationalInsuranceNumber_nino" 
                               name="nationalInsuranceNumber" 
                               value="${nationalInsuranceNumber}" 
                               maxLength="19" 
                               class="ni-number" 
                               autocomplete="off">
    
                        <p class="form-hint">This is on your NI number card, benefit letter, payslip or P60.</p>
                    </li>
    
                    <li class="form-group ">
                        <fieldset class="question-group">
                            <legend id="dateOfBirth_questionLabel" class="form-label-bold">Date of birth </legend>
                            <p class="form-hint" id="dateOfBirth_defaultDateContextualHelp">For example, 16 5 1976</p>
                            <!-- datePlaceholder -->
                            
                            <ul class="form-date" id="dateOfBirth">
                                <li class="form-group">
                                    <label for="dateOfBirth_day">Day</label>
                                    <input type="tel" 
                                           class="form-control"
                                           id="dateOfBirth_day"
                                           name="dateOfBirth_day"
                                           title="Must be numbers only"
                                           value="${dateOfBirth_day}" maxLength="2"
                                           autocomplete="off
                                           ">
                                </li>
                                
                                <li class="form-group month">
                                    <label for="dateOfBirth_month">Month</label>
                                    <input type="tel"
                                           class="form-control"
                                           id="dateOfBirth_month"
                                           name="dateOfBirth_month"
                                           title="Must be numbers only"
                                           value="${dateOfBirth_month}" 
                                           maxLength="2"
                                           autocomplete="off">
                                </li>
                                
                                <li class="form-group form-group-year">
                                    <label for="dateOfBirth_year">Year</label>
                                    <input type="tel"
                                           class="form-control"
                                           id="dateOfBirth_year"
                                           name="dateOfBirth_year"
                                           title="Must be numbers only"
                                           value="${dateOfBirth_year}" 
                                           maxLength="4"
                                           autocomplete="off">
                                </li>
                            </ul>
                        </fieldset>
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
    
        <script type="text/javascript">
            $ ( function ( ) {
                GOVUK.performance.stageprompt.setupForGoogleAnalytics();
                ;
            } )
        </script>
    </article>
</t:mainPage>    
