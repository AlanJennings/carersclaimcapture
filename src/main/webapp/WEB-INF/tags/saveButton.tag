<%@ tag description="Save for later button Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="isSaveEnabled" %>
<%@ attribute name="showSaveButton" %>
<%@ attribute name="beenInPreview" %>
<%@ attribute name="path" %>
<%@ attribute name="saveForLaterUrl" %>

<c:if test="${isSaveEnabled && showSaveButton && (!beenInPreview || path == '/preview' || path == '/consent-and-declaration/declaration')}" >
    <button type="button" id="save" name="save" class="button" href="${saveForLaterUrl}" ><t:message code="form.save" /></button>
</c:if>
<script type="text/javascript">
    $(document).ready(function(){
        $("#save").click(function(){
            var saveurl=$(this).attr("href");
            $("form").attr( "action", saveurl );
            $("form").submit()
        });
    });
</script>

