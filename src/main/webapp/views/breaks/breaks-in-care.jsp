<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Breaks from care" section="Section 7 of 11" backLink="${previousPage}">
        <t:panel id="breaks" label="Breaks already added">
            
            <c:forEach items="breaksInCare" var="break">
                <li id="break.${break.id}">
                    <h3 class="heading-small">${break.startDateDisplayDate}</h3>
                    <dl>
                        <dt>Where were you during the break?</dt><dd>${break.whereCaree}</dd>
                        <dt>Where was the person you care for during the break?</dt><dd>${break.whereYou}</dd>
                        <dt>Medical treatment</dt><dd>${break.hasBreakEnded}</dd>
                    </dl>
                    <div class="actions">
                        <input type="button" name="changerow" id="changerow_${break.id}" value="${break.id}" aria-label="Change button">
                        <input type="button" name="deleterow" id="deleterow_${break.id}" value="${break.id}" aria-label="Delete button">
                    </div>
                </li>
            </c:forEach>
            
        </t:panel>
    </t:pageContent>

</t:mainPage>


