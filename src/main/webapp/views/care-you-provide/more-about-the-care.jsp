<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="The care you give" section="Section 6 of 11" backLink="${previousPage}">
        <t:yesnofield id="spent35HoursCaring" 
                      name="spent35HoursCaring" 
                      value="${spent35HoursCaring}"
                      label="Do you spend 35 hours or more each week caring for John Smith?"   <%-- TODO put display name in here --%>
                      hintBefore="This means any time you look after them personally and incudes things like 
                                  cooking meals or helping them with their shopping" 
                      errors="${validationErrors}" 
        />
        
        <t:group name="otherResidenceGroup" 
                 label="Since 16 October 2015, have you or John Smith been in any of the following for at least a week"
                 hintBefore="Choose <b>all</b> that apply to you." 
                 errors="${validationErrors}">
            
            <t:checkbox id="otherResidence_hospital" 
                        name="otherResidence_hospital" 
                        checkedValue="yes" 
                        value="${otherResidence_hospital}" 
                        text="Hospital" 
                        outerClass="no-class"
                        errors="${validationErrors}"
            />
                        
            <t:checkbox id="otherResidence_respite" 
                        name="otherResidence_respite" 
                        checkedValue="yes" 
                        value="${otherResidence_respite}" 
                        text="Respite or care home" 
                        outerClass="no-class"
                        errors="${validationErrors}"
            />
                        
            <t:checkbox id="otherResidence_none" 
                        name="otherResidence_none" 
                        checkedValue="yes" 
                        value="${otherResidence_none}" 
                        text="None" 
                        outerClass="no-class"
                        errors="${validationErrors}"
            />

       </t:group>
       
       <t:yesnofield id="weeksNotCaring" 
                      name="weeksNotCaring" 
                      value="${weeksNotCaring}"
                      label="Have there been any other weeks you've not provided care for John Smith for 35 hours a week"   <%-- TODO put display name in here --%>
                      hintBefore="This includes time on holiday" 
                      errors="${validationErrors}" 
        />
        
    </t:pageContent>

</t:mainPage>    
