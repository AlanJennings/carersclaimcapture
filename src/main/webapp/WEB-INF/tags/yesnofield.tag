<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
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
        <ul class="inline " id="${id}">  
            <li>            
                <div class="block-label" <%-- onmousedown="trackEvent('${currentPage}','${id}','Yes');" --%>>
                    <input type="radio" 
                           id="${id}_yes" 
                           name="${name}" 
                           <%-- onmousedown="trackEvent('${currentPage}','${id}','Yes');" --%> 
                           value="yes"  
                           <c:if test="${value=='yes'}">checked</c:if>  
                    />
                    <span>Yes</span>
                </div>
            </li>
                
            <li>                                     
                <div class="block-label" <%-- onmousedown="trackEvent('${currentPage}','${id}','No');" --%>
                >
                    <input type="radio" 
                           id="${id}_no" 
                           name="${name}" 
                           <%-- onmousedown="trackEvent('${currentPage}','${id}','No');" --%> 
                           value="no"  
                           <c:if test="${value=='no'}">checked</c:if>  
                    />
                    <span>No</span>
                </div>
            </li>
        </ul>
        ${hintAfter}
    </fieldset>

    ${warningText}
</li>
