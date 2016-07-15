<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t"  tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="breaks-in-care" backLink="${previousPage}">
        
        <!-- breaksInCare = '<c:out value="${breaks}" />' -->
        <c:if test="${not empty breaks}">
            <c:set var="moreBreaksLabel" value="Have you had any more breaks from caring for this person since ${dateOfClaim}?" />
            
            <t:panel id="breaks">
                
                <c:forEach items="${breaks}" var="careBreak">
                    <!-- careBreak = '<c:out value="${careBreak}" />'  -->
                    <li id="break_${careBreak['break_id']}">
                        <h3 class="heading-small">${careBreak['startDate_day']}/${careBreak['startDate_month']}/${careBreak['startDate_year']}</h3>
                        <dl>
                            <dt>Where were you during the break?</dt><dd class="lowercase">${fn:replace(careBreak['whereYou'], '_', ' ')}</dd>
                            <dt>Where was the person you care for during the break?</dt><dd class="lowercase">${fn:replace(careBreak['whereCaree'], '_', ' ')}</dd>
                            <dt>Medical treatment</dt><dd>${careBreak['medicalCareDuringBreak']}</dd>
                        </dl>
                        <div class="actions">
                            <button type="submit" id="changerow_${careBreak['break_id']}" class="actionButton" name="changeBreak" value="${careBreak['break_id']}" aria-label="Change button">Change</button>
                            <button type="submit" id="deleterow_${careBreak['break_id']}" class="actionButton" name="deleteBreak" value="${careBreak['break_id']}" aria-label="Delete button">Delete</button>
                        </div>
                    </li>
                </c:forEach>
                
            </t:panel>
        </c:if>


        <t:yesnofield name="moreBreaksInCare" />
        
        <t:hiddenPanel id="moreBreaksInCareWrap" triggerId="moreBreaksInCare" triggerValue="yes">
            <t:radiobuttons name="moreBreaksInCareResidence" optionValues="hospital|respite|somewhere else" optionLabelKeys="hospital|respite|elsewhere" />
        </t:hiddenPanel>
        
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/s_breaks/g_breaksInCare.js' />"></script>
</t:mainPage>


