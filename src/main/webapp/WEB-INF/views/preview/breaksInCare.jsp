<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<t:accordion label="preview.breaks.heading" openLabel="open-breaks-in-care" closeLabel="close-breaks-in-care" track="true">
	<t:fieldWithLink id="breaks_breaktype" name="breaktype_first.label" value="${anyBreakMessage}" displayChangeButton="${displayChangeButton}" link="${anyBreakLink}" />
	<c:if test="${hasBreaksForTypeHospital}" >
        <t:fieldWithLink id="breaks_hospital" name="otherResidence_hospital.text" value="${hospitalBreakMessage}" displayChangeButton="${displayChangeButton}" link="${hospitalBreakLink}" />
    </c:if>
    <c:if test="${hasBreaksForTypeCareHome}" >
        <t:fieldWithLink id="breaks_carehome" name="otherResidence_respite.text" value="${careHomeBreakMessage}" displayChangeButton="${displayChangeButton}" link="${careHomeBreakLink}" />
    </c:if>
    <t:fieldWithLink id="breaks_breaktype_other" name="breaktype_other_first.label" value="${otherBreakMessage}" displayChangeButton="${displayChangeButton}" link="${otherBreakLink}" />
</t:accordion>
