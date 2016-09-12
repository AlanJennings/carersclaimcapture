<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="page.been-employed" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.been-employed" backLink="${previousPage}">

        <c:if test="${not empty employment}">
            <t:panel id="employment">
                
                <c:forEach items="${employment}" var="employmentPosition">
                    <!-- employmentPosition = '<c:out value="${employmentPosition}" />'  -->
                    <li id="employment_${employmentPosition['employment_id']}">
                        <h3 class="heading-small">${employmentPosition['employerName']}</h3>
                        <dl>
                            <dt><t:message code="employment.columnName.startDate" /></dt>
                            <c:if test="${empty employmentPosition['jobStartDate_day'] || employmentPosition['jobStartDate_day'] == ''}">
                                <dd class="lowercase"><t:message code="employment.text.before" /> ${dateOfClaim_day}/${dateOfClaim_month}/${dateOfClaim_year}</dd>
                            </c:if>
                            <c:if test="${not empty employmentPosition['jobStartDate_day'] && employmentPosition['jobStartDate_day'] != ''}">
                                <dd class="lowercase">${employmentPosition['jobStartDate_day']}/${employmentPosition['jobStartDate_month']}/${employmentPosition['jobStartDate_year']}</dd>
                            </c:if>
                        </dl>
                        <div class="actions">
                            <button type="submit" id="changerow_${employmentPosition['employment_id']}" class="actionButton" name="changeEmployment" value="${employmentPosition['employment_id']}" aria-label="Change button">Change</button>
                            <button type="submit" id="deleterow_${employmentPosition['employment_id']}" class="actionButton" name="deleteEmployment" value="${employmentPosition['employment_id']}" aria-label="Delete button">Delete</button>
                        </div>
                    </li>
                </c:forEach>
                
            </t:panel>
        </c:if>

        <t:panel id="moreEmploymentPanel" innerClass="noClass">
            <t:yesnofield name="moreEmployment" />
        </t:panel>
        
    </t:pageContent>

</t:mainPage>    
