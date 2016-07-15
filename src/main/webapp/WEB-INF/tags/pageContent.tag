<%-- See http://docs.oracle.com/javaee/5/tutorial/doc/bnamu.html --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="page" required="true" %>
    
<%@ attribute name="pageTitleKey" %>
<%@ attribute name="pageTitle" %>
<%@ attribute name="pageSectionKey" %>
<%@ attribute name="backLink" %>
<%@ attribute name="nextButtonTextKey" %>
    
<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.controller.AbstractFormController.ValidationSummary"%>

<t:defaultValue value="${pageScope.nextButtonTextKey}" defaultValue="next" var="nextButtonTextKey" />

<t:defaultValue value="${pageScope.pageTitleKey}" defaultValue="${page}.pageTitle" var="pageTitleKey" />
<t:defaultValue value="${pageScope.pageSectionKey}" defaultValue="${page}.section" var="defaultPageSectionKey" />
<c:if test="not empty pageScope.defaultPageSectionKey">
    <c:set var="pageSectionKey" value="${pageScope.defaultPageSectionKey}" />
</c:if>

<c:if test="not empty pageScope.pageTitleKey">
    <c:set var="pageTitle"><t:message code="${pageScope.pageTitleKey}" parentName="${pageScope.page}" element="pageTitle" /></c:set>
</c:if>

<%-- ************************** start 'pageContent' context object ************************************ --%>

<%-- We need to use 'jsp:useBean' to create the map in the first place, as 'variable' can't create variables directly --%>
<jsp:useBean id="_pageContentMap" class="java.util.HashMap" scope="request"/>

<%-- Expose a context object for this tag (i.e. pageContext) to allow for communication with nested tag (errors in particular) --%>
<%@ variable name-given="pageContent" declare="false" variable-class="java.lang.Map" scope="NESTED" %> <%-- TODO this is not working, the use:Bean and c:set is doing ALL the work --%>
<c:set var="pageContent" value="${_pageContentMap}" scope="request"/>
<c:set target="${pageContent}" property="errors" value="${pageScope.errors}"/>

<%-- *************************** end 'pageContent' context object ************************************* --%>

<main class="container" role="main" id="main">
    <div class="carers-allowance clearfix">

<%--    
                <section class="js-message">
                    <h2>Javascript is currently disabled</h2>
                    <p>For full functionality of this website it is necessary to enable JavaScript, not doing so will result in a reduced user experience.</p>
                </section>
--%>
        <div class="helper-mobile" id="helper-toggle">
            <a class="help">Get help</a>
        </div>
    
        <div class="grid-row main-grid">

            <article class="column-three-quarters main-content eligible">
                
                <c:if test="${not empty pageScope.backLink}">
                    <nav class="back-nav" role="navigation">
                        <a id="topBackButton" href="<c:url value='${pageScope.backLink}' />">Back</a>
                    </nav>
                </c:if>
                
                <h1 class="form-title heading-large">
                    <c:if test="${not empty pageScope.pageSectionKey}">
                        <span class="section-progress"><t:message code="${pageScope.pageSectionKey}" /></span>
                    </c:if>
                    ${pageScope.pageTitle}
                </h1>
                
                <form action="<c:url value='${pageScope.currentPage}' />" method="POST" role="form" style="margin-bottom: 30px;">
                    <input type="hidden" name="csrfToken" value="1b8e21408cf8c53aeaf62e523f4af3e8b7fe4b6a-1460720627654-dfd779df76ec152c0ae4d491"/>    
                    
                    <t:validationSummary errors="${pageScope.errors}" />
                    
                    <fieldset class="form-elements form-eligibility">
                        <ul>
                            
                            <jsp:doBody/>
                            
                        </ul>
                    </fieldset>
            
                    <nav class="form-steps" role="navigation">
                        <ul>
                            <li><button type="submit" name="action" value="next" class="button"><t:message code="${pageScope.nextButtonTextKey}" /></button></li>
                        </ul>
                    </nav>   
                </form>
                
                <c:if test="${not empty pageScope.backLink}">
                    <nav class="back-nav" role="navigation">
                        <a id="bottomBackButton" href="<c:url value='${pageScope.backLink}' />">Back</a>
                    </nav>
                </c:if>
                             
            </article>
            
        </div>
    </div>
</main>
