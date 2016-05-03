<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<footer class="global-footer" id="global-footer">
    <div class="container clearfix">
        <div>
            <nav class="footer-nav language">
                <a id="lang-cy" 
                   href="/change-language/cy" 
                   aria-label="Newid yr iaith diofyn i Gymraeg" 
                   onmousedown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Language Selection Welsh&#x27;);" 
                   onkeydown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Language Selection Welsh&#x27;);"
                >Cymraeg</a>                       
                
                <a id="cookies" 
                   rel="external" 
                   target="_blank" 
                   href="/cookies/en" 
                   onmousedown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Cookies - from footer&#x27;);" 
                   onkeydown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Cookies - from footer&#x27;);"
                >Cookies</a>

                <a id="privacy" 
                   rel="external" 
                   target="privacyLink" 
                   href="https://www.gov.uk/government/organisations/department-for-work-pensions/about/personal-information-charter" 
                   onmousedown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Privacy&#x27;);" 
                   onkeydown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Privacy&#x27;);"
                >Privacy</a>

                <a id="claim-feedback" 
                   href="https://www.gov.uk/done/apply-carers-allowance" 
                   rel="external" 
                   target="_blank"  
                   onmousedown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Feedback&#x27;);" 
                   onkeydown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Feedback&#x27;);"
                >Feedback</a>

            </nav>
            <p>Made in Preston by Department for Work &amp; Pensions</p>
            <p class="ogl">
                All content is available under the 
                    <a rel="external" 
                       href="http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3"
                       target="openGovLink" 
                        onmousedown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Open Government Licence&#x27;);" 
                        onkeydown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Open Government Licence&#x27;);"
                    >Open Government Licence v3.0</a>
                , except where otherwise stated</p>
        </div>
        <div class="fr">
            <a class="crown" 
               href="http://www.nationalarchives.gov.uk/information-management/our-services/crown-copyright.htm" 
               target="crownLink" 
               onmousedown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Crown Copyright&#x27;);" 
               onkeydown="trackEvent(&#x27;${param.trackEventPath}&#x27;,&#x27;Crown Copyright&#x27;);">&copy; Crown Copyright</a>
        </div>
    </div>
</footer>
<!--/ footer -->
