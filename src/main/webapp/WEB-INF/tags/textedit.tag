<%-- See http://docs.oracle.com/javaee/5/tutorial/doc/bnamu.html --%>
<%@ tag description="Text Edit box Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="name" required="true"%>
    
<%@ attribute name="id"%>
<%@ attribute name="tagNested" %>
<%@ attribute name="outerClass"%>
<%@ attribute name="outerStyle"%>
<%@ attribute name="labelKey"%>
<%@ attribute name="labelKeyArgs" %>
<%@ attribute name="hintBeforeKey"%>
<%@ attribute name="hintAfterKey"%>
<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>
    
<%@ attribute name="value"%>
<%@ attribute name="useRawValue"%>
<%@ attribute name="maxLength"%>
<%@ attribute name="additionalClasses"%>

    <script type="text/javascript">
        // Note: NOT "errors", 
        // we cannot do this in a tag as it changes the pageContext
        var attributeNames = ["name", 
                              "id", 
                              "tagNested", 
                              "outerClass", 
                              "outerStyle", 
                              "labelKey", 
                              "labelKeyArgs", 
                              "hintBeforeKey", 
                              "hintAfterKey", 
                              "value", 
                              "useRawValue", 
                              "maxLength", 
                              "additionalClasses"];
        var attributes = {};
        for(var index in attributeNames) {
            var attrName = attributeNames[index];
            attributes[attrName] = null;
        }
        <c:forEach items="${pageScope}" var="p">
            <c:if test="${p.value.getClass().getName()=='java.lang.String'}" >
                if(attributeNames.indexOf("${p.key}") != -1) {
                    attributes["${p.key}"] = "${p.value}";
                }
            </c:if>
        </c:forEach>
        
        if(adminInterface) {
            adminInterface.setDesignTimeInfo("${pageScope.name}", attributeNames, attributes);
            console.log("adminInterface.getDesignTimeInfo");
            console.log(adminInterface.getDesignTimeInfo("${pageScope.name}"));
        }
    </script>


<t:defaultValue value="${pageScope.id}" defaultValue="${pageScope.name}" var="id" />
<t:defaultValue value="${pageScope.useRawValue}" defaultValue="false" var="useRawValue" />

<%-- If not using raw values, then use the name attribute to locate the value --%>
<c:if test="${pageScope.useRawValue!='true'}" >
    <c:set var="value" value="${requestScope[pageScope.name]}" />
</c:if>

<t:component tagType="textedit"
             tagNested="${pageScope.tagNested}"
             name="${pageScope.name}" 
             outerClass="${pageScope.outerClass}" 
             outerStyle="${pageScope.outerStyle}" 
             errors="${pageScope.errors}">

    <label class="form-label-bold" for="${pageScope.id}"> <t:message code="${pageScope.labelKey}" parentName="${pageScope.name}" element="label" args="${pageScope.labelKeyArgs}" /> </label>
    <t:hint hintTextKey="${pageScope.hintBeforeKey}" parentName="${pageScope.name}" element="hintBefore"/>
    <input type="text" class="form-control ${pageScope.additionalClasses}" id="${pageScope.id}" name="${pageScope.name}" value="${pageScope.value}" maxLength="${pageScope.maxLength}" autocomplete="off">
    <t:hint hintTextKey="${pageScope.hintAfterKey}" parentName="${pageScope.name}" element="hintAfter" />

</t:component>


