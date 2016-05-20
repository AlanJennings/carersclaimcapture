<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- <c:out value="${errors}"/> -->

<t:mainPage>
    <article class="column-three-quarters main-content eligible">
        <h1 class="form-title heading-large">
            Can you get Carer's Allowance?
        </h1>
        
        <form action="<c:url value='${currentPage}' />" method="POST" role="form">
            <input type="hidden" name="csrfToken" value="1b8e21408cf8c53aeaf62e523f4af3e8b7fe4b6a-1460720627654-dfd779df76ec152c0ae4d491"/>    
            <jsp:include page="../validationSummary.jsp" />
            <fieldset class="form-elements form-eligibility">
                <ul>
                    
                    <c:if test="${errors.hasError('benefitsAnswer')}" >
                        <c:set var="benefitsAnswer_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group <c:out value='${benefitsAnswer_AdditionalClass}'/>">
                        <c:if test="${errors.hasError('benefitsAnswer')}" >
                            <p class="validation-message">${errors.getErrorMessage('benefitsAnswer')}</p>
                        </c:if>
                        
                        <fieldset class="question-group">
                            <legend class="form-label-bold">What benefit does the person you care for get?</legend>
                            <p class="form-hint">Don't include any benefits they've applied for if they haven't got a decision yet.</p>
                            
                            <ul class="form-group form-group-compound" id="benefitsAnswer">
                                <li>           
                                    <div class="block-label " 
                                         onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?','Personal Independence Payment (PIP) daily living component');"
                                    >
                                        <input type="radio" 
                                               id="benefitsAnswer_PIP" 
                                               name="benefitsAnswer" 
                                               value="PIP"  
                                               <c:if test="${benefitsAnswer=='PIP'}">checked</c:if>                                           
                                               onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?','Personal Independence Payment (PIP) daily living component');"
                                        />
                                        <span>Personal Independence Payment (PIP) daily living component</span>
                                   </div>
                                </li>
                
                                <li>            
                                    <label class="block-label" 
                                           for="benefitsAnswer_DLA" 
                                           onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Disability Living Allowance (DLA) - middle or highest care rate');"
                                    >
                                        <input type="radio" 
                                               id="benefitsAnswer_DLA" 
                                               name="benefitsAnswer" 
                                               value="DLA"  
                                               <c:if test="${benefitsAnswer=='DLA'}">checked</c:if>  
                                               onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Disability Living Allowance (DLA) - middle or highest care rate');"  
                                        />
                                        <span>Disability Living Allowance (DLA) - middle or highest care rate</span>
                                    </label>
                                </li>
                
                                <li>            
                                    <label class="block-label" 
                                           for="benefitsAnswer_AA" 
                                           onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Attendance Allowance (AA)');"
                                    >
                                        <input type="radio" 
                                               id="benefitsAnswer_AA" 
                                               name="benefitsAnswer" 
                                               value="AA"  
                                               <c:if test="${benefitsAnswer=='AA'}">checked</c:if>  
                                               onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Attendance Allowance (AA)');"
                                        />
                                        <span>Attendance Allowance (AA)</span>
                                    </label>
                                </li>
                
                                <li>
                                    
                                    <label class="block-label" 
                                           for="benefitsAnswer_CAA"  
                                           onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Constant Attendance Allowance (CAA)');"
                                    >
                                        <input type="radio" 
                                               id="benefitsAnswer_CAA" 
                                               name="benefitsAnswer" 
                                               value="CAA"  
                                               <c:if test="${benefitsAnswer=='CAA'}">checked</c:if>  
                                               onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Constant Attendance Allowance (CAA)');"  
                                        />
                                        <span>Constant Attendance Allowance (CAA)</span>
                                    </label>
                                </li>
                
                                <li>           
                                    <label class="block-label" 
                                           for="benefitsAnswer_AFIP" 
                                           onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Armed Forces Independence Payment (AFIP)');"
                                    >
                                        <input type="radio" 
                                               id="benefitsAnswer_AFIP" 
                                               name="benefitsAnswer"
                                               value="AFIP" 
                                               <c:if test="${benefitsAnswer=='AFIP'}">checked</c:if>  
                                               onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'Armed Forces Independence Payment (AFIP)');"  
                                        />
                                        <span>Armed Forces Independence Payment (AFIP)</span>
                                    </label>
                                </li>
                
                                <li>            
                                    <label class="block-label" 
                                           for="benefitsAnswer_NONE" 
                                           onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'None of these benefits');"
                                    >
                                        <input type="radio" 
                                               id="benefitsAnswer_NONE" 
                                               name="benefitsAnswer" 
                                               value="NONE" 
                                               <c:if test="${benefitsAnswer=='NONE'}">checked</c:if>  
                                               onmousedown="trackEvent('${currentPage}','What benefit does the person you care for get?', 'None of these benefits');"
                                        />
                                        <span>None of these benefits</span>
                                    </label>
                                </li>
                            </ul>
                        </fieldset>
            
                        <div id="answerNoMessageWrap" class="prompt breaks-prompt validation-summary" style="display: none;">
                            <p>You'll only get Carer's Allowance if the person you care for gets one of these benefits.</p>
                        </div>            
                    </li>
                </ul>
            </fieldset>
    
            <nav class="form-steps" role="navigation">
                <ul>
                    <li><button type="submit" name="action" value="next" class="button">Next</button></li>
                </ul>
            </nav>   
        </form>
        
        <script type="text/javascript" src="<c:url value='/assets/javascript/s_eligibility/answerNoBenefitsMessage.js' />" ></script>
        <script type="text/javascript">
            $(function () {
                window.trackEvent = function(arg1, arg2) {
                    // do nothing
                };
                
                window.initEvents("benefitsAnswer", "NONE");
                trackEvent("times", "claim - eligibility");
                setCookie("claimeligibility",new Date().getTime());                       
                GOVUK.performance.stageprompt.setupForGoogleAnalytics()
            });
        </script>
            
    </article>
</t:mainPage>        


