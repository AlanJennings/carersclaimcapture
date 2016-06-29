<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Additional information" section="Section 11 of 11" backLink="${previousPage}">

        <t:yesnofield id="anythingElse" 
                          name="anythingElse" 
                          value="${anythingElse}"
                          label="Do you want to tell us any additional information about your claim?" 
                          hintBefore="Don't include any medical information or details about care."
                          errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="anythingElseWrap" triggerId="anythingElse" triggerValue="yes">
            <t:textarea id="anythingElseText" 
                        name="anythingElseText" 
                        value="${anythingElseText}" 
                        maxLength="3000" 
                        showRemainingChars="true"
                        label="Tell us anything else you think we should know about your claim" 
                        errors="${validationErrors}"
            />
        </t:hiddenPanel>

        <t:yesnofield id="welshCommunication" 
                          name="welshCommunication" 
                          value="${welshCommunication}"
                          label="Do you live in Wales and want to receive future communications in Welsh?" 
                          errors="${validationErrors}" 
        />
        
    </t:pageContent>

</t:mainPage>    
