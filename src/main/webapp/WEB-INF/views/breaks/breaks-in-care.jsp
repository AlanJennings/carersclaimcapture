<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t"  tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 

<!DOCTYPE html>
<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <c:if test="${not empty breaks}">
            <t:panel id="breaks">
                <h2 class="heading-medium" id="summary-heading"><t:message code="breaks.summary.title" /></h2>
                <table id="summary-table">
                    <tr>
                        <th><t:message code="breaks.summary.who" /></th>
                        <th><t:message code="breaks.summary.where" /></th>
                        <th><t:message code="breaks.summary.from" /></th>
                        <th><t:message code="breaks.summary.to" /></th>
                        <th></th>
                    </tr>
                    
                    <c:forEach items="${breaks}" var="careBreak">
                        <!-- breakInCareType = ${careBreak.breakInCareType} -->
                        <!-- careBreak = '<c:out value="${careBreak}" />'  -->
                        <c:if test="${careBreak['breakInCareType']=='hospital'}">
                            <tr id="break_${careBreak['break_id']}">
                                <c:if test="${careBreak['hospitalBreakWhoInHospital']=='Carer'}">
                                    <td>${carerFirstName} ${carerSurname}</td>
                                    <td><t:message code="otherResidence_hospital.text" /></td>
                                    <td>
                                        ${cads:dateOffset(careBreak['hospitalBreakCarerHospitalStartDate_day'], 
                                                                   careBreak['hospitalBreakCarerHospitalStartDate_month'], 
                                                                   careBreak['hospitalBreakCarerHospitalStartDate_year'], 
                                                                   'd MMMMMMMMMM yyyy', 
                                                                   '')} 
                                    </td>
                                    <td> 
                                        ${cads:dateOffset(careBreak['hospitalBreakCarerHospitalEndDate_day'], 
                                                                   careBreak['hospitalBreakCarerHospitalEndDate_month'], 
                                                                   careBreak['hospitalBreakCarerHospitalEndDate_year'], 
                                                                   'd MMMMMMMMMM yyyy', 
                                                                   '')}
                                    </td>
                                </c:if>
                                <c:if test="${careBreak['hospitalBreakWhoInHospital']=='Caree'}">
                                    <td>${careeFirstName} ${careeSurname}</td>
                                    <td><t:message code="otherResidence_hospital.text" /></td>
                                    <td>
                                        ${cads:dateOffset(careBreak['hospitalBreakCareeHospitalStartDate_day'], 
                                                                   careBreak['hospitalBreakCareeHospitalStartDate_month'], 
                                                                   careBreak['hospitalBreakCareeHospitalStartDate_year'], 
                                                                   'd MMMMMMMMMM yyyy', 
                                                                   '')} 
                                    </td>
                                    <td>
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
                                    <td><t:message code="otherResidence_respite.text" /></td>
                                    <td>
                                        ${cads:dateOffset(careBreak['respiteBreakCarerRespiteStartDate_day'], 
                                                                   careBreak['respiteBreakCarerRespiteStartDate_month'], 
                                                                   careBreak['respiteBreakCarerRespiteStartDate_year'], 
                                                                   'd MMMMMMMMMM yyyy', 
                                                                   '')} 
                                    </td>
                                    <td>
                                        ${cads:dateOffset(careBreak['respiteBreakCarerRespiteEndDate_day'], 
                                                                   careBreak['respiteBreakCarerRespiteEndDate_month'], 
                                                                   careBreak['respiteBreakCarerRespiteEndDate_year'], 
                                                                   'd MMMMMMMMMM yyyy', 
                                                                   '')}
                                    </td>
                                </c:if>
                                <c:if test="${careBreak['respiteBreakWhoInRespite']=='Caree'}">
                                    <td>${careeFirstName} ${careeSurname}</td>
                                    <td><t:message code="otherResidence_respite.text" /></td>
                                    <td>
                                        ${cads:dateOffset(careBreak['respiteBreakCareeRespiteStartDate_day'], 
                                                                   careBreak['respiteBreakCareeRespiteStartDate_month'], 
                                                                   careBreak['respiteBreakCareeRespiteStartDate_year'], 
                                                                   'd MMMMMMMMMM yyyy', 
                                                                   '')} 
                                    </td>
                                    <td>
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
                                </td>
                                <td>
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

        <t:group name="moreBreaksInCareResidenceGroup">
            <t:checkbox name="breaks_hospital" outerClass="no-class" />
            <t:checkbox name="breaks_respite" outerClass="no-class" />
            <t:checkbox name="breaks_none" outerClass="no-class" checkedValue="none" />
        </t:group>
        <t:yesnofield name="moreBreaksInCare" />
        
    </t:pageContent>
    
</t:mainPage>


