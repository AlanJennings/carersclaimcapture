<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="The care you give - times in respite or a care home" section="Section 6 of 11" backLink="${previousPage}">
        
        <t:htmlsection>
            <p>You told us that you or the person you care for have been in respite or a care home.  You could still be paid Carer's Allowance for this time</p>
        </t:htmlsection>
        
        <t:radiobuttons id="whoInRespite" 
                        name="whoInRespite" 
                        optionValues="Carer|Caree"
                        optionLabels="You|John Smith"
                        value="${whoInRespite}"
                        label="Who was in respite or a care home" 
                        errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="whoInRespiteWrapCarer" triggerId="whoInRespite" triggerValue="Carer">
            
            <t:datefield id="carerRespiteStartDate" 
                         nameDay="carerRespiteStartDate_day" 
                         nameMonth="carerRespiteStartDate_month" 
                         nameYear="carerRespiteStartDate_year" 
                         valueDay="${carerRespiteStartDate_day}" 
                         valueMonth="${carerRespiteStartDate_month}" 
                         valueYear="${carerRespiteStartDate_year}" 
                         label="When were you admitted?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 3 5 2016"
            />
        
           <t:yesnofield id="carerRespiteStayEnded" 
                          name="carerRespiteStayEnded" 
                          value="${carerRespiteStayEnded}"
                          label="Have did your respite or care home stay end?"   <%-- TODO put display name in here --%>
                          errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="carerRespiteStayEnded" triggerId="carerRespiteStayEnded" triggerValue="You">
            
                <t:datefield id="carerRespiteEndDate" 
                             nameDay="carerRespiteEndDate_day" 
                             nameMonth="carerRespiteEndDate_month" 
                             nameYear="carerRespiteEndDate_year" 
                             valueDay="${carerRespiteEndDate_day}" 
                             valueMonth="${carerRespiteEndDate_month}" 
                             valueYear="${carerRespiteEndDate_year}" 
                             label="When did your respite or care home stay end?" 
                             errors="${validationErrors}" 
                             hintBefore="For example, 3 5 2016"
                />
                
                <t:yesnofield id="carerRespiteStayMedicalCare" 
                              name="carerRespiteStayMedicalCare" 
                              value="${carerRespiteStayMedicalCare}"
                              label="Did you or the person being looked after receive care from a medical professional during this time?"   <%-- TODO put display name in here --%>
                              errors="${validationErrors}" 
                />
            </t:hiddenPanel>
            
        </t:hiddenPanel>

        <t:hiddenPanel id="whoInRespiteWrapCaree" triggerId="whoInRespite" triggerValue="Caree">
            
            <t:datefield id="careeRespiteStartDate" 
                         nameDay="careeRespiteStartDate_day" 
                         nameMonth="careeRespiteStartDate_month" 
                         nameYear="careeRespiteStartDate_year" 
                         valueDay="${careeRespiteStartDate_day}" 
                         valueMonth="${careeRespiteStartDate_month}" 
                         valueYear="${careeRespiteStartDate_year}" 
                         label="When were they admitted?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 3 5 2016"
            />
        
           <t:yesnofield id="careeRespiteStayEnded" 
                          name="careeRespiteStayEnded" 
                          value="${careeRespiteStayEnded}"
                          label="Has the respite or care home stay ended"   <%-- TODO put display name in here --%>
                          errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="careeRespiteStayEnded" triggerId="careeRespiteStayEnded" triggerValue="You">
            
                <t:datefield id="careeRespiteEndDate" 
                             nameDay="careeRespiteEndDate_day" 
                             nameMonth="careeRespiteEndDate_month" 
                             nameYear="careeRespiteEndDate_year" 
                             valueDay="${careeRespiteEndDate_day}" 
                             valueMonth="${careeRespiteEndDate_month}" 
                             valueYear="${careeRespiteEndDate_year}" 
                             label="When did their respite or care home stay end?" 
                             errors="${validationErrors}" 
                             hintBefore="For example, 3 5 2016"
                />
                
                <t:yesnofield id="careeRespiteStayMedicalCare" 
                              name="careeRespiteStayMedicalCare" 
                              value="${careeRespiteStayMedicalCare}"
                              label="Did you or the person being looked after receive care from a medical professional during this time?"   <%-- TODO put display name in here --%>
                              errors="${validationErrors}" 
                />

                <t:yesnofield id="careeRespiteCarerStillCaring" 
                              name="careeRespiteCarerStillCaring" 
                              value="${careeRespiteCarerStillCaring}"
                              label="During this time in hospital were you still providing care for John Smith for 35 hours a week?"   <%-- TODO put display name in here --%>
                              hintBefore="This could include personal care you provide" 
                              errors="${validationErrors}" 
                />
                            
                
            </t:hiddenPanel>
            
        </t:hiddenPanel>
       
       <t:yesnofield id="weeksNotCaring" 
                      name="weeksNotCaring" 
                      value="${weeksNotCaring}"
                      label="Have there been any other weeks you've not provided care for John Smith for 35 hours or more each week"   <%-- TODO put display name in here --%>
                      hintBefore="This includes time on holiday" 
                      errors="${validationErrors}" 
        />
        
    </t:pageContent>

</t:mainPage>    
