<%@ tag description="Footer Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="currentPage" required="true" type="java.lang.String"%>

<footer class="global-footer" id="global-footer">
    <div class="container clearfix">
        <div>
            <nav class="footer-nav language">
                <c:if test="${isOriginGB && (currentPage eq \"/allowance/benefits\" || currentPage eq \"/circumstances/report-changes/change-selection\")}" >
                    <c:if test="${language eq \"Welsh\"}" >
                        <a id="lang-en"
                           href="/allowance/benefits?lang=en"
                           aria-label="<t:message code="english.helper" />"
                           onmousedown="trackEvent('${pageScope.currentPage}','Language Selection English');"
                           onkeydown="trackEvent('${pageScope.currentPage}','Language Selection English');"><t:message code="english" /></a>
                    </c:if>
                    <c:if test="${language eq \"English\"}" >
                        <a id="lang-cy"
                           href="/allowance/benefits?lang=cy"
                           aria-label="<t:message code="welsh.helper" />"
                           onmousedown="trackEvent('${pageScope.currentPage}','Language Selection Welsh');"
                           onkeydown="trackEvent('${pageScope.currentPage}','Language Selection Welsh');"><t:message code="welsh" /></a>
                    </c:if>
                </c:if>
                <a id="cookies" 
                   href="/cookies/en" 
                   rel="external" 
                   target="_blank" 
                   aria-label="<t:message code="footer.external.link" />"
                   onmousedown="trackEvent('${pageScope.currentPage}','Cookies - from footer');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Cookies - from footer');" 
                ><t:message code="cookies.link" /></a>

                <a id="privacy" 
                   href="https://www.gov.uk/government/organisations/department-for-work-pensions/about/personal-information-charter" 
                   rel="external" 
                   target="privacyLink" 
                   aria-label="<t:message code="footer.external.link" />"
                   onmousedown="trackEvent('${pageScope.currentPage}','Privacy');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Privacy');" 
                ><t:message code="privacy.link" /></a>

                <a id="claim-feedback" 
                   href="https://www.gov.uk/done/apply-carers-allowance" 
                   rel="external" 
                   target="_blank" 
                   aria-label="<t:message code="footer.external.link" />"
                   onmousedown="trackEvent('${pageScope.currentPage}','Feedback');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Feedback');" 
                ><t:message code="footer.feedback.link" /></a>
            </nav>

            <p><t:message code="footer.helpline" /></p>
            <c:if test="${isOriginGB}" >
                <p><t:message code="footer.callcharges" /></p>
            </c:if>
            <p><t:message code="madePreston" /></p>
            <p class="ogl">
                <t:message code="openGovLicensea" />
                <a rel="external" 
                   href="http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3" 
                   target="openGovLink" 
                   aria-label="This link opens in a new window"
                   onmousedown="trackEvent('${pageScope.currentPage}','Open Government Licence');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Open Government Licence');" 
                ><t:message code="openGovLicenseb" /></a>
                <t:message code="openGovLicensec" />
            </p>
        </div>
        <div class="fr">
            <a class="crown" 
               href="http://www.nationalarchives.gov.uk/information-management/our-services/crown-copyright.htm" 
               target="crownLink" 
               onmousedown="trackEvent('${pageScope.currentPage}','Crown Copyright');" 
               onkeydown="trackEvent('${pageScope.currentPage}','Crown Copyright');"
            >&copy; <t:message code="crownCopyright" /></a>
        </div>
    </div>
</footer>