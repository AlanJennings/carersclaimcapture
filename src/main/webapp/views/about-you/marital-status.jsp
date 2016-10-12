<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage>

    <t:pageContent errors="${validationErrors}" backLink="${previousPage}">
        
        <t:radiobuttons name="maritalStatus" 
                        optionValues="Married or civil partner|Single|Divorced or civil partnership dissolved|Widowed or surviving civil partner|Separated|Living with partner"
                        optionLabelKeys="married|single|divorced|widowed|seperated|withPartner"
        />
    
    </t:pageContent>
    
</t:mainPage>
