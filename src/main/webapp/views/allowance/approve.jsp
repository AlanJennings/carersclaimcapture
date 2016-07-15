<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="page.approve.pageTitle" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="page.approve" backLink="${previousPage}">

        <input type="hidden" id="allowedToContinue" name="allowedToContinue" value="true"/>
        <input type="hidden" id="js" name="jsEnabled" value="false">

        <t:htmlsection name="approvalIncomeThreshold"> 
            <ul class="list-bullet">
                <li><t:message code="approvalIncomeThreshold.bullet1" /></li>
                <li><t:message code="approvalIncomeThreshold.bullet2" /></li>
            </ul>
        </t:htmlsection> 

        <t:htmlsection name="approvalDocumentationNeeded">
            <ul class="list-bullet">
                <li><t:message code="approvalDocumentationNeeded.bullet1" /></li>
                <li><t:message code="approvalDocumentationNeeded.bullet2" /></li>
                <li><t:message code="approvalDocumentationNeeded.bullet3" /></li>
                <li><t:message code="approvalDocumentationNeeded.bullet4" /></li>
            </ul> 
        </t:htmlsection>

    </t:pageContent>

</t:mainPage>