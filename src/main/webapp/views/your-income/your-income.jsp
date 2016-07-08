<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %> 
<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Your income" section="Section 9 of 11" backLink="${previousPage}">

        <t:yesnofield id="beenSelfEmployedSince1WeekBeforeClaim" 
                      name="beenSelfEmployedSince1WeekBeforeClaim" 
                      value="${beenSelfEmployedSince1WeekBeforeClaim}"
                      label="Have you been in self-employment since ${cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, 'dd MMMMMMMMMM yyyy', '-1week')}?" 
                      errors="${validationErrors}" 
        />
        
        <t:yesnofield id="beenEmployedSince6MonthsBeforeClaim" 
                      name="beenEmployedSince6MonthsBeforeClaim" 
                      value="${beenEmployedSince6MonthsBeforeClaim}"
                      label="Have you been in employment since ${cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, 'dd MMMMMMMMMM yyyy', '-6months')}?" 
                      errors="${validationErrors}" 
        />
        
        <t:yesnofield id="hadOtherIncomeSinceClaimDate" 
                      name="hadOtherIncomeSinceClaimDate" 
                      value="${hadOtherIncomeSinceClaimDate}"
                      label="Have you had other income since ${cads:dateOffset(dateOfClaim_day, dateOfClaim_month, dateOfClaim_year, 'dd MMMMMMMMMM yyyy', '')}?" 
                      hintBefore="<span style='display: inline-block;'>Statutory Sick Pay, Statutory Paternity, Maternity or Adoption Pay</span>
                                  <span style='display: inline-block;'>Fostering Allowance, Direct payment for caring for someone</span>
                                  <span style='display: inline-block;'>or any other income (rental income etc.)</span>"
                      errors="${validationErrors}" 
        />
        
        <t:hiddenPanel id="otherIncomeWrap" triggerId="hadOtherIncomeSinceClaimDate" triggerValue="yes">
            <t:group name="otherIncomeGroup" hintBefore="Choose <b>all</b> that apply to you." errors="${validationErrors}">
                <t:checkbox id="yourIncome_sickpay" 
                            name="yourIncome_sickpay" 
                            checkedValue="yes" 
                            value="${yourIncome_sickpay}" 
                            text="Statutory Sick Pay" 
                            outerClass="no-class"
                            errors="${validationErrors}"
                />
                            
                <t:checkbox id="yourIncome_patmatadoppay" 
                            name="yourIncome_patmatadoppay" 
                            checkedValue="yes" 
                            value="${yourIncome_patmatadoppay}" 
                            text="Statutory Paternity, Maternity or Adoption Pay" 
                            outerClass="no-class"
                            errors="${validationErrors}"
                />
                            
                <t:checkbox id="yourIncome_fostering" 
                            name="yourIncome_fostering" 
                            checkedValue="yes" 
                            value="${yourIncome_fostering}" 
                            text="Fostering Allowance" 
                            outerClass="no-class"
                            errors="${validationErrors}"
                />
                            
                <t:checkbox id="yourIncome_directpay" 
                            name="yourIncome_directpay" 
                            checkedValue="yes" 
                            value="${yourIncome_directpay}" 
                            text="Direct payment for caring for someone" 
                            outerClass="no-class"
                            errors="${validationErrors}"
                />
                            
                <t:checkbox id="yourIncome_anyother" 
                            name="yourIncome_anyother" 
                            checkedValue="yes" 
                            value="${yourIncome_anyother}" 
                            text="Any other income, eg rental income" 
                            outerClass="no-class"
                            errors="${validationErrors}"
               />
           </t:group>
        </t:hiddenPanel>

    </t:pageContent>

</t:mainPage>    
