<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="id" required="true" type="java.lang.String"%>
<%@attribute name="name" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="yesLabel" required="false" type="java.lang.String"%>
<%@attribute name="noLabel" required="false" type="java.lang.String"%>

<%@attribute name="hintBefore" required="false" type="java.lang.String"%>
<%@attribute name="hintAfter" required="false" type="java.lang.String"%>
<%@attribute name="warningText" required="false" type="java.lang.String"%><!-- TODO change this to noWarningtext, also add yesWarningText -->

<%@attribute name="hasError" required="false" type="java.lang.Object"%>
<%@attribute name="errorMessage" required="false" type="java.lang.String"%>

<!-- TODO add track events separately using jquery -->
<!-- TODO add javascript for the warning text -->


<%-- set default values for yes & no labels --%>
<c:if test="${empty yesLabel}">
    <c:set var="yesLabel" value="Yes" />
</c:if>
<c:if test="${empty noLabel}">
    <c:set var="noLabel" value="No" />
</c:if>

<c:if test="${hasError}" >
    <c:set var="errorClass" value="validation-error" />
</c:if>
<li class="form-group <c:out value='${errorClass}'/>">
    <c:if test="${hasError}" >
        <p class="validation-message">${errorMessage}</p>
    </c:if>

    <fieldset class="question-group">
        <legend class="form-label-bold ">${label}</legend>        

        ${hintBefore}
        <ul class="inline " id="${id}">  
            <li>
                <%-- 
                    clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control with a label
                    instead of a div makes the whole control click-able, not just the tiny checkbox in the middle 
                --%>
                <label class="block-label">
                    <input type="radio" 
                           id="${id}_yes" 
                           name="${name}" 
                           value="yes"  
                           <c:if test="${value=='yes'}">checked</c:if>  
                    />
                    <span><c:out value="${yesLabel}" /></span>
                </label>
            </li>
                
            <li>                                     
                <%-- 
                    clicking on a label is the same as clicking on an input (to gain focus probably), so by spanning the control with a label
                    instead of a div makes the whole control click-able, not just the tiny checkbox in the middle 
                --%>            
                <label class="block-label">
                    <input type="radio" 
                           id="${id}_no" 
                           name="${name}" 
                           value="no"  
                           <c:if test="${value=='no'}">checked</c:if>  
                    />
                    <span><c:out value="${noLabel}" /></span>
                </label>
            </li>
        </ul>
        ${hintAfter}
    </fieldset>

    ${warningText}
</li>
