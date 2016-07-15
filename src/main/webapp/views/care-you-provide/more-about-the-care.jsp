<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" page="more-about-the-care" backLink="${previousPage}">
        <t:yesnofield name="spent35HoursCaring" />

        <t:group name="otherResidenceGroup" >
            <t:checkbox name="otherResidence_hospital" outerClass="no-class" />
            <t:checkbox name="otherResidence_respite" outerClass="no-class" />
            <t:checkbox name="otherResidence_none" outerClass="no-class" />
       </t:group>

       <t:yesnofield name="weeksNotCaring" />

    </t:pageContent>

</t:mainPage>    
