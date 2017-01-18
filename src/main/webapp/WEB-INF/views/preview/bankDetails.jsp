<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:accordion label="preview.bankDetails.title" openLabel="open-bank-details" closeLabel="close-bank-details" track="true">
    <t:fieldWithLink id="bank_details" name="preview.bankDetails.label" value="${bankDetailsEntered}" displayChangeButton="${displayChangeButton}" link="${bankDetailsLink}" />
</t:accordion>

