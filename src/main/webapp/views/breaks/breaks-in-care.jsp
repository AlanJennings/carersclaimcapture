<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t"  tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Breaks from care" section="Section 7 of 11" backLink="${previousPage}">
        
        <!-- breaksInCare = '<c:out value="${breaks}" />' -->
        <c:if test="${empty breaks}">
            <c:set var="moreBreaksLabel" value="Have you had any breaks from caring for this person since ${dateOfClaim}?" />
        
            <t:htmlsection>
                <p>
                    A break is any time you spent less than 35 hours a week caring for the other person. 
                    For example when:
                </p>
            </t:htmlsection>
            
            <t:htmlsection>
                <ul class="list-bullet">
                    <li>they were in respite care, hospital or on holiday without you.</li>
                    <li>you were in hospital or on holiday without them.</li>
                </ul>
            </t:htmlsection>
            
            <t:htmlsection>
                <p>
                    You might still get Carer's Allowance for these times. 
                    <a rel="external" 
                       href="/claim-help#3"
                       target="_blank"
                       onmousedown="trackEvent('/breaks/breaks-in-care','Claim Notes - Breaks from Caring');"
                       onkeydown="trackEvent('/breaks/breaks-in-care','Claim Notes - Breaks from Caring');"
                    >Find out more</a>.
                </p>
            </t:htmlsection>

        </c:if>

        <c:if test="${not empty breaks}">
            <c:set var="moreBreaksLabel" value="Have you had any more breaks from caring for this person since ${dateOfClaim}?" />
            <t:panel id="breaks" label="Breaks already added">
                
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

        <t:panel id="moreBreaks" label="Breaks from care">
            <t:yesnofield id="moreBreaksInCare" 
                          name="moreBreaksInCare" 
                          value="${moreBreaksInCare}"
                          label="${moreBreaksLabel}" 
                          errors="${validationErrors}" 
            />
        </t:panel>
        
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/s_breaks/g_breaksInCare.js' />"></script>
</t:mainPage>


