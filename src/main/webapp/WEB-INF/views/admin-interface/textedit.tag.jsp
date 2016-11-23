<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:textedit name="${name}" 
            tagNested="${tagNested}" 
            outerClass="${outerClass}"
            outerStyle="${outerStyle}"
            labelKey="${labelKey}"
            labelKeyArgs="${labelKeyArgs}" 
            hintBeforeKey="${hintBeforeKey}"
            hintAfterKey="${hintAfterKey}"
            value="${value}"
            useRawValue="${useRawValue}"
            maxLength="${maxLength}" 
            additionalClasses="${additionalClasses}"
/> 
