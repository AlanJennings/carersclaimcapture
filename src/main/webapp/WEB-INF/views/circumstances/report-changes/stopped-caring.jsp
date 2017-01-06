<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage page="" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="" backLink="${previousPage}">

        <t:htmlsection name="stoppedProvidingCare"></t:htmlsection>

        <t:datefield name="dateStoppedProvidingCare" />
        <t:textarea name="stoppedProvidingCareFurtherDetails" />

    </t:pageContent>

</t:mainPage>