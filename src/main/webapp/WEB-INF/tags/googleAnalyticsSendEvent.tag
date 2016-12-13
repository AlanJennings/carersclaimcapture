<%@ tag description="Hint Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="action" required="true" %>
<%@ attribute name="label" required="true" %>
<script type="text/javascript">
    $(document).ready(function() {
        googleAnalytics.sendTrackEvent("${pageScope.action}", "${pageScope.label}");
    });
</script>