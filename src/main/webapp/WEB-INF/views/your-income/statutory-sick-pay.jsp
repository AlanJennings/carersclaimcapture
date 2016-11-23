<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>
    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:yesnofield name="sickPayStillBeingPaidThisPay" />
        
        <t:hiddenPanel id="sickPaySstillBeingPaidThisPayWrap" triggerId="sickPayStillBeingPaidThisPay" triggerValue="no">
            <t:datefield name="sickPayWhenDidYouLastGetPaid" />
        </t:hiddenPanel>

        <t:textedit name="sickPayWhoPaidYouThisPay" maxLength="60" />
        <t:textedit name="sickPayAmountOfThisPay" maxLength="12" />
        <t:radiobuttons name="sickPayHowOftenPaidThisPay" optionValues="Weekly|Fortnightly|Four-Weekly|Monthly|Other" />

        <t:hiddenPanel id="sickPayHowOftenPaidThisPayWrap" triggerId="sickPayHowOftenPaidThisPay" triggerValue="Other">
            <t:textedit name="sickPayHowOftenPaidThisPayOther" maxLength="60" />
        </t:hiddenPanel>
        
    </t:pageContent>

</t:mainPage>    




