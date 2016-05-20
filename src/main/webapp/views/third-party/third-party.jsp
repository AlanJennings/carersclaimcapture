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
            <span class="section-progress"> Section 1 of 11 </span> 
            Are you applying for Carer's Allowance for yourself?
        </h1>
    
        <form action="<c:url value='${currentPage}' />" method="POST" role="form">
            <jsp:include page="../validationSummary.jsp" />
            <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />
    
            <fieldset class="form-elements">
                <ul>
                
                    <c:if test="${errors.hasError('thirdParty')}" >
                        <c:set var="thirdParty_AdditionalClass" value="validation-error" />
                    </c:if>
                    <li class="form-group <c:out value='${thirdParty_AdditionalClass}'/>">
                        <c:if test="${errors.hasError('thirdParty')}" >
                            <p class="validation-message">${errors.getErrorMessage('thirdParty')}</p>
                        </c:if>
                    
                        <fieldset>
                            <legend id="thirdParty_questionLabel" class="form-label-bold ">Are you the carer?</legend>
                            <ul class="form-group form-group-compound" id="thirdParty">
                                <li>
                                    <label class="block-label"
                                           for="thirdParty_yesCarer"
                                           onmousedown="trackEvent('${currentPage}','thirdParty.tracking','Yes, you&#39;re the carer');">
                                        <input type="radio"
                                               id="thirdParty_yesCarer"
                                               name="thirdParty"
                                               value="yesCarer" 
                                               <c:if test="${thirdParty=='yesCarer'}">checked</c:if>
                                               onmousedown="trackEvent('${currentPage}','thirdParty.tracking','Yes, you&#39;re the carer');"
                                        /> 
                                        <span>Yes, you're the carer</span>
                                    </label>
                                </li>
    
                                <li>
                                    <label class="block-label"
                                           for="thirdParty_noCarer"
                                           onmousedown="trackEvent('${currentPage}','thirdParty.tracking','No, you&#39;re applying on behalf of the carer');">
                                        <input type="radio"
                                               id="thirdParty_noCarer"
                                               name="thirdParty"
                                               value="noCarer" 
                                               <c:if test="${thirdParty=='noCarer'}">checked</c:if>
                                               onmousedown="trackEvent('${currentPage}','thirdParty.tracking','No, you&#39;re applying on behalf of the carer');"
                                        />
                                        <span>No, you're applying on behalf of the carer</span>
                                    </label>
                                </li>
                            </ul>
                        </fieldset>
                    </li>

                    <li id="thirdPartyWrap" class="form-group">
                        <ul class="extra-group">

                            <c:if test="${errors.hasError('nameAndOrganisation')}" >
                                <c:set var="thirdParty_nameAndOrganisation_AdditionalClass" value="validation-error" />
                            </c:if>
                            <li class="form-group <c:out value='${thirdParty_nameAndOrganisation_AdditionalClass}'/>">
                                <c:if test="${errors.hasError('nameAndOrganisation')}" >
                                    <p class="validation-message">${errors.getErrorMessage('nameAndOrganisation')}</p>
                                </c:if>

                                <label id="thirdParty_nameAndOrganisation_questionLabel"
                                       class="form-label-bold"
                                       for="thirdParty_nameAndOrganisation">Your name and organisation </label>

                                <input type="text"
                                       class="form-control "
                                       id="thirdParty_nameAndOrganisation"
                                       name="nameAndOrganisation"
                                       value="${nameAndOrganisation}"
                                       maxLength="60"
                                       autocomplete="off">

                                <p class="form-hint">
                                    Fill the rest of the form in as if you're the carer. For
                                    example, if asked for 'your address' put the address of
                                    the person doing the caring.
                                </p>
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
        
        <script type="text/javascript" src="<c:url value='/assets/javascript/third_party/thirdParty.js' />"></script>
        <script type="text/javascript">
            $(function () {
                trackEvent ( "times", "claim - start" );
                setCookie("claimstart", new Date().getTime());
                
            });
            
            $(function() {
                window.initEvents("thirdParty_yesCarer", "thirdParty_noCarer", "thirdParty_nameAndOrganisation")
                trackEvent ( "times", "claim - start" );
                setCookie("claimstart", new Date().getTime());
            });
        </script>
    </article>
</t:mainPage>                