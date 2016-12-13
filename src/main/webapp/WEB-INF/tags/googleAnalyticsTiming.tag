<%@ tag description="Google Send Event with category, action and label parameters" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="tracktype" required="true" %>
<script type="text/javascript">
    <%-- We track 3 journeys thus 6 tracktypes
        a) Claim from eligibility to end STARTELIGIBLE ... ENDELIGIBLE
        b) Claim from start to end STARTCLAIM ... ENDCLAIM
        c) Circs from start to end STARTCIRCS ... ENDCIRCS
    --%>
    $(document).ready(function () {
        switch("${pageScope.tracktype}"){
            case "STARTELIGIBLE":
                setCookie("claimeligibility", new Date().getTime());
                googleAnalytics.sendTrackEvent("times", "claim - eligibility", "");
                break;
            case "STARTCLAIM":
                setCookie("claimstart", new Date().getTime());
                googleAnalytics.sendTrackEvent("times", "claim - start", "");
                break;
            case "ENDCLAIM":
                googleAnalytics.sendTrackEvent("times", "claim - end", "");
                var eligibility = getCookie('claimeligibility');
                var start = getCookie('claimstart');
                var now = new Date().getTime();
                googleAnalytics.sendTrackEvent("TimeToCompletion", "Claim from eligibility", now - eligibility);
                googleAnalytics.sendTrackEvent("TimeToCompletion", "Claim from start", now - start);
                break;
            case "STARTCIRCS":
                setCookie("circsstart", new Date().getTime());
                googleAnalytics.sendTrackEvent("times", "circs - start", "");
                break;
            case "ENDCIRCS":
                googleAnalytics.sendTrackEvent("times", "circs - end", "");
                var start = getCookie('circsstart');
                googleAnalytics.sendTrackEvent("TimeToCompletion", "circs from start", new Date().getTime() - start);
                break;
            default:
                // ERROR what to do ??
        }
    });
</script>
