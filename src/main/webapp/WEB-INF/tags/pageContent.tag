<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="pageTitle" required="true" type="java.lang.String"%>
<%@attribute name="section" required="false" type="java.lang.String"%>
<%@attribute name="backLink" required="false" type="java.lang.String"%>
<%@attribute name="nextButtonText" required="false" type="java.lang.String"%>

<c:if test="${empty nextButtonText}">
    <c:set var="nextButtonText" value="Next" />
</c:if>


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

            <!-- <c:out value="${errors}"/> -->
            <article class="column-three-quarters main-content eligible">
                
                <c:if test="${not empty backLink}">
                    <nav class="back-nav" role="navigation">
                        <a id="topBackButton" name="topBackButton" href="<c:url value='${backLink}' />">Back</a>
                    </nav>
                </c:if>
                
                <h1 class="form-title heading-large">
                    <c:if test="${not empty section}">
                        <span class="section-progress">${section}</span>
                    </c:if>
                    ${pageTitle}
                </h1>
                
                <form action="<c:url value='${currentPage}' />" method="POST" role="form" style="margin-bottom: 30px;">
                    <input type="hidden" name="csrfToken" value="1b8e21408cf8c53aeaf62e523f4af3e8b7fe4b6a-1460720627654-dfd779df76ec152c0ae4d491"/>    
                    <jsp:include page="../validationSummary.jsp" />
                    <fieldset class="form-elements form-eligibility">
                        <ul>
                            
                            <jsp:doBody/>
                            
                        </ul>
                    </fieldset>
            
                    <nav class="form-steps" role="navigation">
                        <ul>
                            <li><button type="submit" name="action" value="next" class="button">${nextButtonText}</button></li>
                        </ul>
                    </nav>   
                </form>
                
                <c:if test="${not empty backLink}">
                    <nav class="back-nav" role="navigation">
                        <a id="bottomBackButton" name="bottomBackButton" href="<c:url value='${backLink}' />">Back</a>
                    </nav>
                </c:if>
                             
            </article>
            
        </div>
    </div>
</main>
