<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui.1.12.0.js' />" ></script>
<script type="text/javascript" src="<c:url value='/javascript/adminInterface.js' />" ></script>

<script type="text/javascript">
    $('#adminInterface').show();
    $( document ).ready(function() {
        adminInterface.init();
    });   
</script>
