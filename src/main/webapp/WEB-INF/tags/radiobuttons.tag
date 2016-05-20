<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="optionIds" required="true" type="java.lang.String"%>
<%@attribute name="optionValues" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="warningText" required="false" type="java.lang.String"%>

<%@attribute name="hasError" required="false" type="java.lang.Object"%>
<%@attribute name="errorMessage" required="false" type="java.lang.String"%>

<!-- TODO add track events separately using jquery -->

<c:if test="${hasError}" >
    <c:set var="additionalClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${additionalClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <fieldset class="question-group">
        <legend class="form-label-bold ">${label}</legend>        

        ${hintBefore}
        <ul class="form-group form-group-compound" id="${id}">
        
            <c:forTokens items="${optionIds}" delims="|" var="optionId" varStatus="optionIdIndex">
               <li>                
                    <div class="block-label" for="${id}_${optionId}" onmousedown="">
                        <input type="radio" 
                               id="${id}_${optionId}" 
                               name="${name}" 
                               onmousedown=""  
                               value="${optionId}"  
                               <c:if test="${value==optionId}">checked</c:if>  
                        >
                        <%-- A bit inefficient, but less hacky than the alternatives --%>
                        <c:forTokens items="${optionValues}" delims="|" var="optionValue" varStatus="optionValueIndex">
                            <c:if test="${optionIdIndex.index==optionValueIndex.index}">
                                <span>${optionValue}</span>
                            </c:if>
                        </c:forTokens>
                    </div>
                </li>
            </c:forTokens>

        </ul>
        ${hintAfter}
    </fieldset>

    ${warningText}
</li>
