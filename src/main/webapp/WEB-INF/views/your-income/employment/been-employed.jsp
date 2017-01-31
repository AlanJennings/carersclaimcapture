<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        <c:if test="${not empty employment}">
            <t:panel id="employment">
                <c:forEach items="${employment}" var="employmentPosition">
                    <!-- employmentPosition = '<c:out value="${employmentPosition}" />'  -->
                    <li id="employment_${employmentPosition['employment_id']}">
                        <h3 class="heading-small">${employmentPosition['employerName']}</h3>
                        <dl>
                            <dt><t:message code="employment.columnName.startDate" /></dt>
                            <c:if test="${empty employmentPosition['jobStartDate_day'] || employmentPosition['jobStartDate_day'] == ''}">
                                <dd><t:message code="employment.text.before" /></dd>
                            </c:if>
                            <c:if test="${not empty employmentPosition['jobStartDate_day'] && employmentPosition['jobStartDate_day'] != ''}">
                                <dd class="lowercase">${employmentPosition['jobStartDate_day']}/${employmentPosition['jobStartDate_month']}/${employmentPosition['jobStartDate_year']}</dd>
                            </c:if>
                        </dl>
                        <div class="actions">
                            <button type="submit" id="changerow_${employmentPosition['employment_id']}" class="actionButton" name="changeSubFormRecord" value="${employmentPosition['employment_id']}" aria-label="<t:message code="change.button" />"><t:message code="change" /></button>
                            <button type="button" id="deleterow_${employmentPosition['employment_id']}" class="actionButton" name="deleteEmployment" value="${employmentPosition['employment_id']}" onclick="window.showPanel('delete_${employmentPosition['employment_id']}', true)" aria-label="<t:message code="delete.button" />"><t:message code="delete" /></button>
                        </div>
                    </li>
                    <t:hiddenWarning id="delete_${employmentPosition['employment_id']}" outerClass="prompt breaks-prompt validation-summary" triggerId="deleterow_${employmentPosition['employment_id']}" triggerValue="changerow_${employmentPosition['employment_id']}">
                        <p class="normalMsg"><t:message code="delete.employment.message" /></p>
                        <button type="button" class="button row secondary" id="noDeleteButton" value="${employmentPosition['employment_id']}" onclick="window.hidePanel('delete_${employmentPosition['employment_id']}', true)" aria-label="<t:message code="no.delete.label" />" ><t:message code="no.delete" /></button>
                        <button type="submit" class="button row warning" id="yesDeleteButton" name="deleteSubFormRecord" value="${employmentPosition['employment_id']}" aria-label="<t:message code="yes.delete.label" />" ><t:message code="yes.delete" /></button>
                    </t:hiddenWarning>
                </c:forEach>
            </t:panel>
        </c:if>
        <t:panel id="moreEmploymentPanel" innerClass="noClass">
            <t:yesnofield name="moreEmployment" />
        </t:panel>
    </t:pageContent>
</t:mainPage>    
