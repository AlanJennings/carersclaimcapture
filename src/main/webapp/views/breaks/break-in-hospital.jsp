<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="The care you give - times in hospitaL" section="Section 6 of 11" backLink="${previousPage}">
        
        <t:htmlsection>
            <p>You told us that you or the person you care for have been in hospital.  You could still be paid Carer's Allowance for this time</p>
        </t:htmlsection>
        
        <t:radiobuttons id="whoInHospital" 
                        name="whoInHospital" 
                        optionValues="Carer|Caree"
                        optionLabels="You|John Smith"
                        value="${whoInHospital}"
                        label="Who was in hospital" 
                        errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="whoInHospitalWrapCarer" triggerId="whoInHospital" triggerValue="Carer">
            
            <t:datefield id="carerHospitalStartDate" 
                         nameDay="carerHospitalStartDate_day" 
                         nameMonth="carerHospitalStartDate_month" 
                         nameYear="carerHospitalStartDate_year" 
                         valueDay="${carerHospitalStartDate_day}" 
                         valueMonth="${carerHospitalStartDate_month}" 
                         valueYear="${carerHospitalStartDate_year}" 
                         label="When were you admitted?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 3 5 2016"
            />
        
           <t:yesnofield id="carerHospitalStayEnded" 
                          name="carerHospitalStayEnded" 
                          value="${carerHospitalStayEnded}"
                          label="Has the hospital stay ended"   <%-- TODO put display name in here --%>
                          errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="carerHospitalStayEndedWrap" triggerId="carerHospitalStayEnded" triggerValue="You">
            
                <t:datefield id="carerHospitalEndDate" 
                             nameDay="carerHospitalEndDate_day" 
                             nameMonth="carerHospitalEndDate_month" 
                             nameYear="carerHospitalEndDate_year" 
                             valueDay="${carerHospitalEndDate_day}" 
                             valueMonth="${carerHospitalEndDate_month}" 
                             valueYear="${carerHospitalEndDate_year}" 
                             label="When did your hospital stay end?" 
                             errors="${validationErrors}" 
                             hintBefore="For example, 3 5 2016"
                />
                
                <t:radiobuttons id="carerInHospitalCareeLocation" 
                                name="carerInHospitalCareeLocation" 
                                optionValues="In hospital|In respite care|Somewhere else"
                                optionLabels="In hospital|In respite care|Somewhere else"
                                value="${carerInHospitalCareeLocation}"
                                label="Where was John Smith during this break?" 
                                errors="${validationErrors}" 
                />
                            
                <t:hiddenPanel id="carerInHospitalCareeLocationWrap" triggerId="carerInHospitalCareeLocation" triggerValue="Somewhere else">
                    
                    <t:textedit id="carerInHospitalCareeLocationText" 
                                name="carerInHospitalCareeLocationText" 
                                value="${carerInHospitalCareeLocationText}" 
                                maxLength="30" 
                                label="Where was John Smith during this break?" 
                                errors="${validationErrors}" 
                    />
                </t:hiddenPanel>
                
            </t:hiddenPanel>
            
        </t:hiddenPanel>

        <t:hiddenPanel id="whoInHospitalWrapCaree" triggerId="whoInHospital" triggerValue="Caree">
            
            <t:datefield id="careeHospitalStartDate" 
                         nameDay="careeHospitalStartDate_day" 
                         nameMonth="careeHospitalStartDate_month" 
                         nameYear="careeHospitalStartDate_year" 
                         valueDay="${careeHospitalStartDate_day}" 
                         valueMonth="${careeHospitalStartDate_month}" 
                         valueYear="${careeHospitalStartDate_year}" 
                         label="When were they admitted?" 
                         errors="${validationErrors}" 
                         hintBefore="For example, 3 5 2016"
            />
        
           <t:yesnofield id="careeHospitalStayEnded" 
                          name="careeHospitalStayEnded" 
                          value="${careeHospitalStayEnded}"
                          label="Has the hospital stay ended"   <%-- TODO put display name in here --%>
                          errors="${validationErrors}" 
            />
            
            <t:hiddenPanel id="careeHospitalStayEnded" triggerId="careeHospitalStayEnded" triggerValue="You">
            
                <t:datefield id="careeHospitalEndDate" 
                             nameDay="careeHospitalEndDate_day" 
                             nameMonth="careeHospitalEndDate_month" 
                             nameYear="careeHospitalEndDate_year" 
                             valueDay="${careeHospitalEndDate_day}" 
                             valueMonth="${careeHospitalEndDate_month}" 
                             valueYear="${careeHospitalEndDate_year}" 
                             label="When did their hospital stay end?" 
                             errors="${validationErrors}" 
                             hintBefore="For example, 3 5 2016"
                />
                
                <t:yesnofield id="careeHospitalCarerStillCaring" 
                              name="careeHospitalCarerStillCaring" 
                              value="${careeHospitalCarerStillCaring}"
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
