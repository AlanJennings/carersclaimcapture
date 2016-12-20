<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="unrecoverable.error">
    <div class="carers-allowance clearfix">
        <div class="grid-row main-grid">
            <article class="column-three-quarters main-content error-statement">
                <h1 class="form-title heading-large"><t:message code="unrecoverable.error.cookie" /></h1>
                <p>
                    <t:message code="unrecoverable.error.cookie.help1" />
                    <a rel="external" target="_blank" href="@routes.Cookies.page(lang.language)" target="_blank"
                        @views.html.ga.trackClickEvent(linkName = "Cookies - from error page")>
                        <t:message code="unrecoverable.error.cookie.help1.anchorText" />
                    </a>
                    <t:message code="unrecoverable.error.cookie.help1.2" />
                </p>
                <p>
                    <button id="Try" type="submit" name="action" value="next" class="button"  @views.html.ga.trackClickEvent(linkName = "Chose try again") onClick="location.href='@url'" aria-label="<t:message code="try.claim.help" />" ><t:message code="try.claim" /></button>
                </p>
                <nav class="form-steps">
                    <ul>
                        <li>@common.feedbackLink(isFooterItem = false) <t:message code="feedback.takes30secs" /></li>
                    </ul>
                </nav>
            </article>
        </div>
    </div>
</t:mainPage>
