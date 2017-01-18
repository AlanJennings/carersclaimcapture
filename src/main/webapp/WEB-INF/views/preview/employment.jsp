<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:fieldWithLink id="employment_been_employed_since" name="beenEmployedSince6MonthsBeforeClaim.label" value="${beenEmployedSince6MonthsBeforeClaim}" displayChangeButton="${displayChangeButton}" link="${beenEmployedSince6MonthsBeforeClaimLink}" />
<c:if test="${isEmployed}" >
    <%@include file="jobsPreview.jsp"%>
</c:if>
