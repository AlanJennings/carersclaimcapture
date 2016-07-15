<%@ tag description="Footer Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="currentPage" required="true" type="java.lang.String"%>

<footer class="global-footer" id="global-footer">
    <div class="container clearfix">
        <div>
            <nav class="footer-nav language">
                <a id="lang-cy" 
                   href="/change-language/cy" 
                   aria-label="Newid yr iaith diofyn i Gymraeg" 
                   onmousedown="trackEvent('${pageScope.currentPage}','Language Selection Welsh');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Language Selection Welsh');"
                >Cymraeg</a>

                <a id="cookies" 
                   href="/cookies/en" 
                   rel="external" 
                   target="_blank" 
                   aria-label="This link opens in a new window"
                   onmousedown="trackEvent('${pageScope.currentPage}','Cookies - from footer');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Cookies - from footer');" 
                >Cookies</a>

                <a id="privacy" 
                   href="https://www.gov.uk/government/organisations/department-for-work-pensions/about/personal-information-charter" 
                   rel="external" 
                   target="privacyLink" 
                   aria-label="This link opens in a new window"
                   onmousedown="trackEvent('${pageScope.currentPage}','Privacy');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Privacy');" 
                >Privacy</a>

                <a id="claim-feedback" 
                   href="https://www.gov.uk/done/apply-carers-allowance" 
                   rel="external" 
                   target="_blank" 
                   aria-label="This link opens in a new window"
                   onmousedown="trackEvent('${pageScope.currentPage}','Feedback');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Feedback');" 
                >Feedback</a>
            </nav>
            <p>Helpline - 0345 608 4321</p>
            <p>Calls cost up to 9p a minute from landlines, calls from mobiles may cost more.</p>
            <p>Made in Preston by Department for Work &amp; Pensions</p>
            <p class="ogl">
                All content is available under the 
                <a rel="external" 
                   href="http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3" 
                   target="openGovLink" 
                   aria-label="This link opens in a new window"
                   onmousedown="trackEvent('${pageScope.currentPage}','Open Government Licence');" 
                   onkeydown="trackEvent('${pageScope.currentPage}','Open Government Licence');" 
                >Open Government Licence v3.0</a>
                , except where otherwise stated
            </p>
        </div>
        <div class="fr">
            <a class="crown" 
               href="http://www.nationalarchives.gov.uk/information-management/our-services/crown-copyright.htm" 
               target="crownLink" 
               onmousedown="trackEvent('${pageScope.currentPage}','Crown Copyright');" 
               onkeydown="trackEvent('${pageScope.currentPage}','Crown Copyright');"
            >&copy; Crown Copyright</a>
        </div>
    </div>
</footer>