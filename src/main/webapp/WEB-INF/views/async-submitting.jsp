<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<!DOCTYPE html>

<t:mainPage pageTitle="async.submitting.title">
    <t:pageContent>
        <input type="hidden" name="${cads:encrypt("transactionId")}" value="${transactionId}"/>
        <input type="hidden" name="${cads:encrypt("isClaim")}" value="${isClaim}"/>
        <section id="jswrapper" hidden="true" class="carers-container">
            <h1 class="form-title heading-large">
                <c:if test="${isClaim}"><t:message code="async.submitting.help1" /></c:if>
                <c:if test="${!isClaim}"><t:message code="async.submitting.help1.circs" /></c:if>
                <span id="dots"></span>
            </h1>
            <div class="prompt submitting">
                <p>
                    <c:if test="${isClaim}"><t:message code="async.submitting.help2" /></c:if>
                    <c:if test="${!isClaim}"><t:message code="async.submitting.help2.circs" /></c:if>
                </p>
                <p><t:message code="async.submitting.help3" /></p>
            </div>
        </section>

        <section id="refresh">
            <h1><t:message code="async.submitting.help1" /><span id="dots1"></span></h1>
            <div class="prompt submitting">
                <p><t:message code="async.submitting.refresh1" /></p>
                <p>
                    <c:if test="${isClaim}"><t:message code="async.submitting.refresh2" /></c:if>
                    <c:if test="${!isClaim}"><t:message code="async.submitting.refresh2.circs" /></c:if>
                </p>
            </div>

            <nav class="form-steps">
                <button type="submit" name="action" value="next" class="button secondary"><t:message code="async.submitting.refresh" /></button>
            </nav>
        </section>
    </t:pageContent>
</t:mainPage>

<script type="text/javascript">
    var n = 0
    function dots(){
        n = n % 4
        $("#dots").html("");
        $("#dots1").html("");
        for(var i = 0; i < n;i++){
            $("#dots").html($("#dots").html()+".");
            $("#dots1").html($("#dots1").html()+".");
        }
        n = n+1
        setTimeout(dots, 500);
    }
    $(function(){
        $("#refresh").hide();
        $("#jswrapper").show();
        setTimeout(function(){$("form").submit()}, 10000);
        setTimeout(dots, 1000);
    })
</script>