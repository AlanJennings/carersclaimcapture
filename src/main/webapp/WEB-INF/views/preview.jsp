<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:mainPage pageTitle="preview.title">
    <main class="container" role="main" id="main">
        <div class="carers-allowance clearfix">
            <div class="grid-row main-grid">
                <article class="column-full-width main-content">
                    <h1 class="form-title heading-large"><t:message code="preview.summary" /></h1>

                    <t:validationSummary errors="${pageScope.errors}" />

                    <p class="text"><t:message code="preview.info" /></p>
                    <c:if test="${disableChangeButton}" >
                        <p class="text helper-prompt"><t:message code="preview.helper" /></p>
                    </c:if>

                    <div class="summary">
                        <%@include file="preview/aboutYou.jsp"%>
                        <%--
                        <%@include file="preview/nationality.jsp"%>
                        <%@include file="preview/yourPartnerDetails.jsp"%>
                        <%@include file="preview/careYouProvide.jsp"%>
                        <%@include file="preview/breaksInCare.jsp"%>
                        <%@include file="preview/education.jsp"%>
                        <%@include file="preview/yourIncome.jsp"%>
                        <c:if test="${showBankDetails}" >
                            <%@include file="preview/bankDetails.jsp"%>
                        </c:if>
                        <%@include file="preview/additionalInfo.jsp"%>
                        --%>
                    </div>

                    <p class="hide-print hide-no-js">
                        <a href="javascript:window.print()" class="button button-print" role="button" value="<t:message code="button.print.summary" />" aria-label="<t:message code="button.print.summary" />">
                            <t:message code="button.print.summary" /></a>
                    </p>

                    <p class="hide-print helper-prompt">
                        <small>
                            <span class="js-hidden"><t:message code="preview.print.noJs" /></span>
                            <t:message code="preview.print" />
                        </small>
                    </p>

                    <form action="<c:url value='${currentPage}' />" method="POST" role="form" style="margin-bottom: 30px;">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="pageName" value="${pageScope.page}"/>
                        <nav class="form-steps">
                            <ul>
                                <li><button type="submit" name="action" id="next" value="next" class="button" aria-label="<t:message code="next" />"><t:message code="next" /></button></li>
                                <c:if test="${showSaveButton}" >
                                    <li><t:saveButton isSaveEnabled="${isSaveEnabled}" showSaveButton="${howSaveButton}" beenInPreview="${beenInPreview}" path="/preview" saveForLaterUrl="${saveForLaterUrl}" /></li>
                                </c:if>
                            </ul>
                        </nav>
                    </form>
                </article>
            </div>
        </div>
    </main>
</t:mainPage>

<!-- Add legal message to footer -->
<section class="footer-print">
	<!-- US1360  -->
	<h1 class='print-only legal-disclaimer'><t:message code="preview.legalDisclaimer" /></h1>
</section>

<!-- Add legal watermark when printing -->
<div class="legal-watermark">
	<img 
		src="/images/reference-watermark.png"
		height="0" width="0" 
		style="visibility: hidden" 
		alt="<t:message code="preview.legalDisclaimer" />"
	/>
</div>


<script type="text/javascript" src="<c:url value='/javascript/preview.js' />" ></script>
<script type="text/javascript">
	$(function(){
		window.trackChange()
	});
</script>
