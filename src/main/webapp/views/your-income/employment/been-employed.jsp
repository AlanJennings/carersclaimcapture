<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Employment" section="Section 9 of 11" backLink="${previousPage}">

        <c:if test="${not empty employment}">
            <t:panel id="employment" label="Breaks already added">
                
                <c:forEach items="${employment}" var="employmentPosition">
                    <!-- employmentPosition = '<c:out value="${employmentPosition}" />'  -->
                    <li id="employment_${employmentPosition['employment_id']}">
                        <h3 class="heading-small">${employmentPosition['employerName']}</h3>
                        <dl>
                            <dt>Start Date</dt>
                            <c:if test="${empty employmentPosition['jobStartDate_day'] || employmentPosition['jobStartDate_day'] == ''}">
                                <dd class="lowercase">Before ${dateOfClaim_day}/${dateOfClaim_month}/${dateOfClaim_year}</dd>
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

        <t:panel id="moreEmploymentPanel" label="Your employment history" innerClass="noClass">
            <t:yesnofield id="moreEmployment" 
                          name="moreEmployment" 
                          value="${moreEmployment}"
                          label="Have you had any other jobs since 24 December 2015?" 
                          hintBefore="This is six months before your claim date."
                          errors="${validationErrors}" 
            />
        </t:panel>
        
    </t:pageContent>

</t:mainPage>    
