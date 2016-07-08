<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Employment" section="Section 9 of 11" backLink="${previousPage}">
        <t:yesnofield id="empAdditionalInfo" 
                          name="empAdditionalInfo" 
                          value="${empAdditionalInfo}"
                          label="Do you want to add anything about your work?" 
                          hintBefore="Don't include any information about your pension, if you get one."
                          errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="empAdditionalInfoWrap" triggerId="empAdditionalInfo" triggerValue="yes">
            <t:textarea id="empAdditionalInfoText" 
                        name="empAdditionalInfoText" 
                        value="${empAdditionalInfoText}" 
                        maxLength="3000" 
                        showRemainingChars="true"
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>
    </t:pageContent>

</t:mainPage>    
