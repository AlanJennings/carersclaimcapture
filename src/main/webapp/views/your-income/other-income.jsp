<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Other income" section="Section 9 of 11" backLink="${previousPage}">
        <t:textarea id="otherPaymentsInfo" 
                    name="otherPaymentsInfo"
                    value="${otherPaymentsInfo}" 
                    maxLength="3000" 
                    showRemainingChars="true" 
                    label="What other income have you had since 1 July 2016?" 
                    hintBefore="Give full details of how often and how much you're paid." 
                    errors="${validationErrors}">
        </t:textarea>
    </t:pageContent>

</t:mainPage>
