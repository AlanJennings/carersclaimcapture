<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="About the person you care for" section="Section 6 of 11" backLink="${previousPage}">
        <t:yesnofield id="spent35HoursCaring" 
                      name="spent35HoursCaring" 
                      value="${spent35HoursCaring}"
                      label="Do you spend 35 hours or more each week caring for this person?" 
                      errors="${validationErrors}" 
        />
    </t:pageContent>

</t:mainPage>    
