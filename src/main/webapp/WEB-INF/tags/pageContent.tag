<%-- See http://docs.oracle.com/javaee/5/tutorial/doc/bnamu.html --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="page" %>
<%@ attribute name="pageTitle" %>
<%@ attribute name="pageTitleKey" %>
<%@ attribute name="pageSectionKey" %>
<%@ attribute name="backLink" %>
<%@ attribute name="nextButtonTextKey" %>

<%@ attribute name="errors" type="uk.gov.dwp.carersallowance.validations.ValidationSummary"%>
<t:defaultValue value="${pageScope.page}" defaultValue="${requestScope['javax.servlet.forward.servlet_path']}" var="page" />
<t:defaultValue value="${pageScope.nextButtonTextKey}" defaultValue="next" var="nextButtonTextKey" />
<t:defaultValue value="${pageScope.pageTitleKey}" defaultValue="${page}.pageTitle" var="pageTitleKey" />

<c:set var="pageSection"><t:message code="${pageScope.pageSectionKey}" parentName="${pageScope.page}" element="section" /></c:set>

<c:if test="${not empty pageScope.pageTitleKey}">
    <c:set var='pageTitle' ><t:message code="${pageScope.pageTitleKey}" parentName="${pageScope.page}" element="pageTitle" /></c:set>
</c:if>

<%--
    If this is uncommented, you can drag controls around, which we probably don't want anymore
    either no editing, or full editing, in which case probably move control using up/down arrows, or index position or something
    <t:adminInterface />
 --%>
<%-- ************************** start 'pageContent' context object ************************************ --%>

<%-- We need to use 'jsp:useBean' to create the map in the first place, as 'variable' can't create variables directly --%>
<jsp:useBean id="_pageContentMap" class="java.util.HashMap" scope="request"/>

<%-- Expose a context object for this tag (i.e. pageContext) to allow for communication with nested tag (errors in particular) --%>
<%@ variable name-given="pageContent" declare="false" variable-class="java.lang.Map" scope="NESTED" %> <%-- TODO this is not working, the use:Bean and c:set is doing ALL the work --%>
<c:set var="pageContent" value="${_pageContentMap}" scope="request"/>
<c:set target="${pageContent}" property="errors" value="${pageScope.errors}"/>
<spring:eval var="isPrototype" expression="@environment.getProperty('prototype.disclaimer')"/>

<%-- *************************** end 'pageContent' context object ************************************* --%>

<main class="container" role="main" id="main">
    <div class="carers-allowance clearfix">
        <c:if test="${isPrototype == true}">
        <div class="prototype">
            <p><t:message code="prototype.message" /></p>
        </div>
        </c:if>
<noscript>
        <section class="js-message">
            <h2><t:message code="noJavascript.title" /></h2>
            <p><t:message code="noJavascript.text" /></p>
        </section>
</noscript>
        
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
                    <c:if test="${not empty pageScope.pageSection}">
                        <span class="section-progress">${pageScope.pageSection}</span>
                    </c:if>
                    ${pageScope.pageTitle}
                </h1>
                
                <form action="<c:url value='${pageScope.currentPage}' />" method="POST" role="form" style="margin-bottom: 30px;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="pageName" value="${pageScope.page}"/>    
                    
                    <t:validationSummary errors="${pageScope.errors}" />
                    
                    <fieldset class="form-elements form-eligibility">
                        <ul data-tag-type="page">
                            
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
