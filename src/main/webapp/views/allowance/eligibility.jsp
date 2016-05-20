<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- <c:out value="${errors}"/> -->

<t:mainPage>
    <article class="column-three-quarters main-content eligible">
    
        <nav class="back-nav" role="navigation">
            <a id="topBackButton" name="topBackButton" href="<c:url value='${previousPage}?changing=true' />">Back</a>
        </nav>

        <h1 class="form-title heading-large">Can you get Carer's Allowance?</h1>
        <form action="<c:url value='${currentPage}' />" method="POST" role="form">    
            <jsp:include page="../validationSummary.jsp" />
            <input type="hidden" name="csrfToken" value="4c040ea5f674dd84171d37ca879c956bd91b3965-1461573007666-37e54fc00423766525e2af12"/>
            <fieldset class="form-elements form-eligibility">
                <ul>
                
                    <!-- over35HoursAWeek -->
                    <c:if test="${errors.hasError('over35HoursAWeek')}" >
                        <c:set var="over35HoursAWeek_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group ${over35HoursAWeek_AdditionalClass} ">
                        <c:if test="${errors.hasError('over35HoursAWeek')}" >
                            <p class="validation-message">${errors.getErrorMessage('over35HoursAWeek')}</p>
                        </c:if>
                        <fieldset class="question-group">
                            <legend  id="hours_answer_questionLabel"  class="form-label-bold ">
                                Do you spend 35 hours or more each week caring for the person you care for?
                            </legend>        
        
                            <p class="form-hint">For example, cooking meals, or helping them with their shopping.</p>
                            <ul class="inline " id="hours_answer">  
                                <li>            
                                    <div class="block-label"                                            
                                         onmousedown="trackEvent('${currentPage}','hours.answer',&quot;Yes&quot;);"
                                    >
                                        <input type="radio" 
                                               id="hours_answer_yes" 
                                               name="over35HoursAWeek" 
                                               onmousedown="trackEvent('${currentPage}','hours.answer',&quot;Yes&quot;);"  
                                               value="yes"  
                                               <c:if test="${over35HoursAWeek=='yes'}">checked</c:if>  
                                        />
                                        <span>Yes</span>
                                    </div>
                                </li>
                                    
                                <li>                                     
                                    <div class="block-label" 
                                           for="hours_answer_no" 
                                           onmousedown="trackEvent('${currentPage}','hours.answer',&quot;No&quot;);"
                                    >
                                        <input type="radio" 
                                               id="hours_answer_no" 
                                               name="over35HoursAWeek" 
                                               onmousedown="trackEvent('${currentPage}','hours.answer',&quot;No&quot;);"  
                                               value="no"  
                                               <c:if test="${over35HoursAWeek=='no'}">checked</c:if>  
                                        />
                                        <span>No</span>
                                    </div>
                                </li>
                            </ul>
                        </fieldset>
            
                        <div id="warninghours_answer" class="validation-summary" style="display: none;">                
                            <p>You can't get Carer's Allowance if you care for someone less than 35 hours a week. You might still be entitled to 
                                <a rel="external" href="https://gov.uk/carers-credit" target="_blank"> Carer's Credit</a>.
                            </p>                
                        </div>
                    </li>

                    <!-- over16YearsOld -->
                    <c:if test="${errors.hasError('over16YearsOld')}" >
                        <c:set var="over16YearsOld_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group ${over16YearsOld_AdditionalClass}">
                        <c:if test="${errors.hasError('over16YearsOld')}" >
                            <p class="validation-message">${errors.getErrorMessage('over16YearsOld')}</p>
                        </c:if>
                        <fieldset class="question-group">
                            <legend  id="over16_answer_questionLabel"  class="form-label-bold ">
                                Are you aged 16 or over?
                            </legend>
                            <ul class="inline " id="over16_answer">   
                                <li>           
                                    <div class="block-label" 
                                         onmousedown="trackEvent('${currentPage}','over16.answer',&quot;Yes&quot;);"
                                    >
                                        <input type="radio" 
                                               id="over16_answer_yes" 
                                               name="over16YearsOld" 
                                               onmousedown="trackEvent('${currentPage}','over16.answer',&quot;Yes&quot;);"  
                                               value="yes"
                                               <c:if test="${over16YearsOld=='yes'}">checked</c:if>  
                                        />
                                        <span>Yes</span>
                                    </div>
                                </li>    
                                <li>                                      
                                    <div class="block-label" 
                                         onmousedown="trackEvent('${currentPage}','over16.answer',&quot;No&quot;);"
                                    >
                                        <input type="radio" 
                                               id="over16_answer_no" 
                                               name="over16YearsOld" 
                                               onmousedown="trackEvent('${currentPage}','over16.answer',&quot;No&quot;);"  
                                               <c:if test="${over16YearsOld=='no'}">checked</c:if>  
                                               value="no"  
                                        />
                                        <span>No</span>
                                    </div>
                                </li>
                            </ul>
                        </fieldset>
                        <div id="warningover16_answer" class="validation-summary" style="display: none;">                
                                <p>You can only get Carer's Allowance if you're 16 or over.</p>                
                        </div>
                    </li>
        
                    <!-- originCountry -->
                    <c:if test="${errors.hasError('originCountry')}" >
                        <c:set var="originCountry_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group ${originCountry_AdditionalClass}">
                        <c:if test="${errors.hasError('originCountry')}" >
                            <p class="validation-message">${errors.getErrorMessage('originCountry')}</p>
                        </c:if>
                        <fieldset class="question-group">                    
                            <legend id="origin_questionLabel" class="form-label-bold ">
                                Which country do you live in?
                            </legend>
                            
                            <ul class="form-group form-group-compound" id="origin">
                                <li>                
                                    <div class="block-label" for="origin_GB" onmousedown="">
                                        <input type="radio" 
                                               id="origin_GB" 
                                               name="originCountry" 
                                               onmousedown=""  
                                               value="GB"  
                                               <c:if test="${originCountry=='GB'}">checked</c:if>  
                                        >
                                        <span>England, Scotland or Wales</span>
                                    </div>
                                </li>
                                <li>                
                                    <div class="block-label" for="origin_NI" onmousedown="">
                                        <input type="radio" 
                                               id="origin_NI" 
                                               name="originCountry" 
                                               onmousedown=""  
                                               value="NI"  
                                               <c:if test="${originCountry=='NI'}">checked</c:if>  
                                        >
                                        <span>Northern Ireland</span>
                                    </div>
                                </li>
                                <li>                
                                    <div class="block-label" for="origin_OTHER" onmousedown="">
                                        <input type="radio" 
                                               id="origin_OTHER" 
                                               name="originCountry" 
                                               onmousedown=""  
                                               value="OTHER"  
                                               <c:if test="${originCountry=='OTHER'}">checked</c:if>  
                                        >
                                        <span>Another country</span>
                                    </div>
                                </li>
                            </ul>
                        </fieldset>
                        <div id="originWarning" class="prompt breaks-prompt validation-summary">
                            <p>You can normally get Carer's Allowance if you live in England, Scotland or Wales. You might get it if you live in the EEA (European Economic Area) or Switzerland, but you must apply to find out.</p>
                        </div>
                    </li>
                </ul>
            </fieldset>

            <nav class="form-steps" role="navigation">
                <ul>
                    <li>
                        <button type="submit" id="next" name="action" value="next" class="button"  >Next</button>
                    </li>       
                    <li>
                        <a id="bottomBackButton" name="bottomBackButton" href="<c:url value='${previousPage}?changing=true' />">Back</a>
                    </li>
                </ul>
            </nav>
        </form>

        <script type="text/javascript" src="<c:url value='/assets/javascript/s_eligibility/answerNoMessage.js' />" ></script>
        <script type="text/javascript">
            $(function () {
                window.initEvents("warninghours_answer","hours_answer_yes", "hours_answer_no");
                window.initEvents("warningover16_answer","over16_answer_yes", "over16_answer_no");
                window.initEvents("warninglivesInGB_answer","livesInGB_answer_yes", "livesInGB_answer_no");
                window.originWarning("origin_GB", "origin_NI", "origin_OTHER", "originWarning");
                window.gaEvents("origin_GB", "origin_NI", "origin_OTHER");
            
                var hoursId = 'hours_answer';
                var res = $('#'+hoursId).parents('li.form-group')
                                        .find('.validation-summary a')
                                        .click(
                                            function(){
                                                trackEvent('allowance/eligibility','carers-credit','clicked');
                                            });
            });
        </script>
    </article>
</t:mainPage>