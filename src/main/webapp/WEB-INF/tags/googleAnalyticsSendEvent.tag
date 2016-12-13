<%@ tag description="Google Send Event with category, action and label parameters" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="category" required="false" %>
<%@ attribute name="action" required="true" %>
<%@ attribute name="label" required="false" %>
<script type="text/javascript">
    $(document).ready(function () {
        <!-- If category is not passed it will be defauted to url in javascript         -->
        <!-- If label is not passed it will be be sent as blank                         -->
        googleAnalytics.sendTrackEvent("${pageScope.category}", "${pageScope.action}", "${pageScope.label}");
    });
</script>