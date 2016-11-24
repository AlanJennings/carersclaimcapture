<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="unrecoverable.error">
    <main class="container" role="main" id="main">
        <div class="carers-allowance clearfix">
            <div class="grid-row main-grid">
                <article class="column-three-quarters main-content error-statement">
                    <h1 class="form-title heading-large"><t:message code="unrecoverable.error.cookie" /></h1>
                    <p><t:message code="unrecoverable.error.cookie.help2" /></p>
                    <!-- @views.html.ga.trackClickEvent(linkName = "Chose try again") onClic-->
                    <p><button id="tryjs" type="submit" name="action" value="next" class="button"   onClick="history.back()" aria-label="<t:message code="trying.claim.help" />" style="display:none;"><t:message code="trying.claim" /></button>
                        <!-- href="@{request.headers.get('Referer').getOrElse(s'$url')}" -->
                       <a id="try" class="button" href="" aria-label="<t:message code="trying.claim.help" />" ><t:message code="trying.claim" /></a></p>
                    <!-- <a @views.html.ga.trackClickEvent(linkName = "Chose start again") href="@url" -->
                    <p><t:message code="unrecoverable.error.cookie.if" /> <a href=""><t:message code="unrecoverable.error.cookie.restart" /></a></p>
                    <nav class="form-steps">
                        <ul>
                            <!-- <li>@common.feedbackLink(isFooterItem = false) -->
                            <li> <t:message code="feedback.takes30secs" /></li>
                        </ul>
                    </nav>
                </article>
            </div>
        </div>
    </main>
    <!-- <script type="text/javascript">$($("#try").hide()); $($("#tryJs").show());</script> -->
</t:mainPage>
