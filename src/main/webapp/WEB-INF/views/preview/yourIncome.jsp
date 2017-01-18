<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:accordion label="/your-income/your-income.pageTitle" openLabel="open-your-income" closeLabel="close-your-income" track="true">
    <%@include file="employment.jsp"%>
    <%@include file="selfEmployment.jsp"%>
    <%@include file="yourIncomeOtherPayments.jsp"%>
	<c:if test="${isSelfEmployed || isEmployed}">
        <t:fieldWithLink id="employment_additional_info" name="empAdditionalInfo.label" value="${previewEmpAdditionalInfo}" displayChangeButton="${displayChangeButton}" link="${empAdditionalInfoLink}" />
    </c:if>
</t:accordion>
