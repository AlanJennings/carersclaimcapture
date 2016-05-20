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
        
        <form action="<c:url value='${currentPage}' />" method="POST" role="form" style="margin-bottom: 30px;">
            <jsp:include page="../validationSummary.jsp" />
            <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />
            <fieldset class="form-elements" data-journey="carers-allowance:page:about-you">
                <ul>
                    
                    <t:textedit id="title" name="title" value="${title}" maxLength="20" label="Title" hasError="${errors.hasError('title')}" errorMessage="${errors.getErrorMessage('title')}" /> 
                    <t:textedit id="firstName" name="firstName" value="${firstName}" maxLength="17" label="First name" hasError="${errors.hasError('firstName')}" errorMessage="${errors.getErrorMessage('firstName')}" />
                    <t:textedit id="middleName" name="middleName" value="${middleName}" maxLength="17" label="Middle name(s)" hasError="${errors.hasError('middleName')}" errorMessage="${errors.getErrorMessage('middleName')}" />
                    <t:textedit id="surname" name="surname" value="${surname}" maxLength="35" label="Last name" hasError="${errors.hasError('surname')}" errorMessage="${errors.getErrorMessage('surname')}" />
                    
                    <!-- We accept a possible space around each character with 9 max in nino ie. AB010203D so 9*2+1 ==> 19 chars max -->
                    <t:textedit id="nationalInsuranceNumber" 
                                name="nationalInsuranceNumber" 
                                value="${nationalInsuranceNumber}" 
                                maxLength="19" 
                                label="National Insurance number" 
                                hasError="${errors.hasError('nationalInsuranceNumber')}" 
                                errorMessage="${errors.getErrorMessage('nationalInsuranceNumber')}" 
                                additionalClasses="ni-number"
                                hintBefore='<p class="form-hint">For example, VO123456D</p>'
                                hintAfter='<p class="form-hint">This is on your NI number card, benefit letter, payslip or P60.</p>'
                    />
                    
                    <t:datefield id="dateOfBirth" 
                                 nameDay="dateOfBirth_day" 
                                 nameMonth="dateOfBirth_month" 
                                 nameYear="dateOfBirth_year" 
                                 valueDay="${dateOfBirth_day}" 
                                 valueMonth="${dateOfBirth_month}" 
                                 valueYear="${dateOfBirth_year}" 
                                 label="National Insurance number" 
                                 hasError="${errors.hasError('dateOfBirth')}" 
                                 errorMessage="${errors.getErrorMessage('dateOfBirth')}" 
                                 hintBefore='<p class="form-hint" id="dateOfBirth_defaultDateContextualHelp">For example, 16 5 1976</p>'
                    />
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
            <!-- i think this might relate to save for later -->
            $(document).ready(function(){
                $("#save").click(function(){
                    var saveurl=$(this).attr("href");
                    var saveurl=$(this).attr("href");
                    $("form").attr( "action", saveurl );
                    $("form").submit()
                });
            });
        </script>
    
        <script type="text/javascript">
            $ ( function ( ) {
                GOVUK.performance.stageprompt.setupForGoogleAnalytics();
                ;
            } )
        </script>
    </article>
</t:mainPage>    
