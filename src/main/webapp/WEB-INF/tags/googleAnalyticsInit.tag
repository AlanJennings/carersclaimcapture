<%@ tag description="Hint Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="trackingId" required="true" %>

<c:set var="gaUrl" value="//www.google-analytics.com/analytics.js"/>
<c:set var="gaUrl" value="//localhost:9021/javascript/analytics.js"/>
<script type="text/javascript" src="/javascript/googleAnalytics.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        googleAnalytics.init({
                    url: "${pageScope.gaUrl}",
                    agentid: "${pageScope.trackingId}"
                }
        );
        googleAnalytics.bindRadioButtons();
    });
</script>