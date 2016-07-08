<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Are you applying for Carer's Allowance for yourself?" section="Section 1 of 11" backLink="${previousPage}"> 
        <%--  Note the values have changed from 'yesCarer/noCarer to yes/no --%>
        <t:radiobuttons id="thirdParty" 
                        name="thirdParty" 
                        optionValues="yes|no"
                        optionLabels="Yes, you're the carer|
                                      No, you're applying on behalf of the carer"
                        value="${thirdParty}"
                        label="Are you the carer?" 
                        errors="${validationErrors}" 
        />

        <t:hiddenPanel id="thirdPartyWrap" triggerId="thirdParty" triggerValue="no">
            <t:textedit id="nameAndOrganisation" 
                        name="nameAndOrganisation" 
                        value="${nameAndOrganisation}" 
                        maxLength="60" 
                        label="Your name and organisation" 
                        errors="${validationErrors}"
                        hintAfter='<p class="form-hint">Fill the rest of the form in as if you&rsquo;re the carer. For
                                      example, if asked for &rsquo;your address&rsquo; put the address of
                                      the person doing the caring.
                                   </p>'
            />
        </t:hiddenPanel>
    </t:pageContent>

    <script type="text/javascript">
        $(document).ready(function(){
            $("#save").click(function(){
                var saveurl=$(this).attr("href");
                var saveurl=$(this).attr("href");
                $("form").attr( "action", saveurl );
                $("form").submit()
            });
        });
    </script>
<%--   
    <script type="text/javascript" src="<c:url value='/assets/javascript/third_party/thirdParty.js' />"></script>
--%> 
    <script type="text/javascript">
        $(function () {
            trackEvent ( "times", "claim - start" );
            setCookie("claimstart", new Date().getTime());
            
        });
        
        $(function() {
            // window.initEvents("thirdParty_yes", "thirdParty_no", "nameAndOrganisation")
            trackEvent ( "times", "claim - start" );
            setCookie("claimstart", new Date().getTime());
        });
    </script>

</t:mainPage>                