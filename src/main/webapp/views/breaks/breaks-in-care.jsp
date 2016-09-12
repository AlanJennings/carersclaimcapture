<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t"  tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 

<!DOCTYPE html>

<t:mainPage page="page.breaks-in-care" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.breaks-in-care" backLink="${previousPage}">
        
        <!-- breaksInCare = '<c:out value="${breaks}" />' -->
        <c:if test="${not empty breaks}">
            <c:set var="moreBreaksLabel" value="Have you had any more breaks from caring for this person since ${dateOfClaim}?" />
            
            <t:panel id="breaks">

                <table>
                    <tr>
                        <th>Who</th>
                        <th>Where</th>
                        <th>When</th>
                        <th></th>
                    </tr>
                    
                    <c:forEach items="${breaks}" var="careBreak">
                        <!-- breakInCareType = ${careBreak.breakInCareType} -->
                        <!-- careBreak = '<c:out value="${careBreak}" />'  -->
                        <c:if test="${careBreak['breakInCareType']=='hospital'}">
                            <tr id="break_${careBreak['break_id']}">
                                <c:if test="${careBreak['hospitalBreakWhoInHospital']=='Carer'}">
                                    <td>${carerFirstName} ${carerSurname}</td>
                                    <td>Hospital</td>
                                    <td>
                                        ${cads:dateOffset(careBreak['hospitalBreakCarerHospitalStartDate_day'], 
                                                          careBreak['hospitalBreakCarerHospitalStartDate_month'], 
                                                          careBreak['hospitalBreakCarerHospitalStartDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')} 
                                        - 
                                        ${cads:dateOffset(careBreak['hospitalBreakCarerHospitalEndDate_day'], 
                                                          careBreak['hospitalBreakCarerHospitalEndDate_month'], 
                                                          careBreak['hospitalBreakCarerHospitalEndDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')}
                                    </td>
                                </c:if>
                                <c:if test="${careBreak['hospitalBreakWhoInHospital']=='Caree'}">
                                    <td>${careeFirstName} ${careeSurname}</td>
                                    <td>Hospital</td>
                                    <td>
                                        ${cads:dateOffset(careBreak['hospitalBreakCareeHospitalStartDate_day'], 
                                                          careBreak['hospitalBreakCareeHospitalStartDate_month'], 
                                                          careBreak['hospitalBreakCareeHospitalStartDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')} 
                                        - 
                                        ${cads:dateOffset(careBreak['hospitalBreakCareeHospitalEndDate_day'], 
                                                          careBreak['hospitalBreakCareeHospitalEndDate_month'], 
                                                          careBreak['hospitalBreakCareeHospitalEndDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')}
                                    </td>
                                </c:if>
                                <td>
                                    <button type="submit" id="changerow_${careBreak['break_id']}" class="actionButton" name="changeBreak" value="${careBreak['break_id']}" aria-label="Change button">Change</button>
                                    <button type="submit" id="deleterow_${careBreak['break_id']}" class="actionButton" name="deleteBreak" value="${careBreak['break_id']}" aria-label="Delete button">Delete</button>
                                </td>
                            </tr>
                        </c:if>
                        
                        <c:if test="${careBreak['breakInCareType']=='respite'}">
                            <tr id="break_${careBreak['break_id']}">
                                <c:if test="${careBreak['respiteBreakWhoInRespite']=='Carer'}">
                                    <td>${carerFirstName} ${carerSurname}</td>
                                    <td>Respite or a Care Home</td>
                                    <td>
                                        ${cads:dateOffset(careBreak['respiteBreakCarerRespiteStartDate_day'], 
                                                          respiteBreakCarerRespiteStartDate_month'], 
                                                          respiteBreakCarerRespiteStartDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')} 
                                        - 
                                        ${cads:dateOffset(careBreak['respiteBreakCarerRespiteEndDate_day'], 
                                                          respiteBreakCarerRespiteEndDate_month'], 
                                                          respiteBreakCarerRespiteEndDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')}
                                    </td>
                                </c:if>
                                <c:if test="${careBreak['respiteBreakWhoInRespite']=='Caree'}">
                                    <td>${careeFirstName} ${careeSurname}</td>
                                    <td>Respite or a Care Home</td>
                                    <td>
                                        ${cads:dateOffset(careBreak['respiteBreakCareeRespiteStartDate_day'], 
                                                          careBreak['respiteBreakCareeRespiteStartDate_month'], 
                                                          careBreak['respiteBreakCareeRespiteStartDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')} 
                                        - 
                                        ${cads:dateOffset(careBreak['respiteBreakCareeRespiteEndDate_day'], 
                                                          careBreak['respiteBreakCareeRespiteEndDate_month'], 
                                                          careBreak['respiteBreakCareeRespiteEndDate_year'], 
                                                          'd MMMMMMMMMM yyyy', 
                                                          '')}
                                    </td>
                                </c:if>
                                <td>
                                    <button type="submit" id="changerow_${careBreak['break_id']}" class="actionButton" name="changeBreak" value="${careBreak['break_id']}" aria-label="Change button">Change</button>
                                    <button type="submit" id="deleterow_${careBreak['break_id']}" class="actionButton" name="deleteBreak" value="${careBreak['break_id']}" aria-label="Delete button">Delete</button>
                                </td>
                            </tr>
                        </c:if>

                        <c:if test="${careBreak['breakInCareType']=='elsewhere'}">
                            <tr id="break_${careBreak['break_id']}">
                                <td></td>
                                <td>Somewhere else</td>
                                <td>
                                    ${cads:dateOffset(careBreak['careeSomewhereElseStartDate_day'], 
                                                      careBreak['careeSomewhereElseStartDate_month'], 
                                                      careBreak['careeSomewhereElseStartDate_year'], 
                                                      'd MMMMMMMMMM yyyy', 
                                                      '')} 
                                    - 
                                    ${cads:dateOffset(careBreak['careeSomewhereElseEndDate_day'], 
                                                      careBreak['careeSomewhereElseEndDate_month'], 
                                                      careBreak['careeSomewhereElseEndDate_year'], 
                                                      'd MMMMMMMMMM yyyy', 
                                                      '')}
                                </td>
                                <td>
                                    <button type="submit" id="changerow_${careBreak['break_id']}" class="actionButton" name="changeBreak" value="${careBreak['break_id']}" aria-label="Change button">Change</button>
                                    <button type="submit" id="deleterow_${careBreak['break_id']}" class="actionButton" name="deleteBreak" value="${careBreak['break_id']}" aria-label="Delete button">Delete</button>
                                </td>
                            </tr>
                        </c:if>
                            
                    </c:forEach>
                </table>
            </t:panel>
        </c:if>

        <t:yesnofield name="moreBreaksInCare" labelKeyArgs="${cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, 'd MMMMMMMMMM yyyy', '')}|${careeFirstName} ${careeSurname}"/>
        
        <t:hiddenPanel id="moreBreaksInCareWrap" triggerId="moreBreaksInCare" triggerValue="yes">
            <t:radiobuttons name="moreBreaksInCareResidence" optionValues="hospital|respite|somewhere else" optionLabelKeys="hospital|respite|elsewhere" />
        </t:hiddenPanel>
        
    </t:pageContent>
    
    <script type="text/javascript" src="<c:url value='/assets/javascript/s_breaks/g_breaksInCare.js' />"></script>
</t:mainPage>


