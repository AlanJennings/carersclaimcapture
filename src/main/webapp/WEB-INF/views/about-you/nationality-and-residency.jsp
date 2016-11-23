<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">

        <t:radiobuttons name="nationality"  optionValues="British|Another nationality" optionLabelKeys="British|Other"/>

        <t:hiddenPanel id="actualNationalityWrap" triggerId="nationality" triggerValue="Another nationality">
            <t:textedit name="actualnationality" maxLength="35" />
        </t:hiddenPanel>

        <t:yesnofield name="alwaysLivedInUK" />

        <t:hiddenPanel id="alwaysLivedInUKNoWrap" triggerId="alwaysLivedInUK" triggerValue="no">
            <t:yesnofield name="liveInUKNow" />

            <t:hiddenPanel id="liveInUKNowYesWrap" triggerId="liveInUKNow" triggerValue="yes">
                <t:radiobuttons name="arrivedInUK" optionValues="less|more" />

                <t:hiddenPanel id="arrivedInUKRecentWrap" triggerId="arrivedInUK" triggerValue="less">
                    <t:datefield name="arrivedInUKDate" />
                    <t:textedit name="arrivedInUKFrom" maxLength="60" />
                </t:hiddenPanel>
            </t:hiddenPanel>
        </t:hiddenPanel>

        <t:yesnofield name="trip52Weeks" />

        <t:hiddenPanel id="trip52WeeksYesWrap" triggerId="trip52Weeks" triggerValue="yes">
            <t:textarea name="tripDetails" maxLength="3000" />
        </t:hiddenPanel>
        
    </t:pageContent>
    
</t:mainPage>    
