<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>What benefit does the person you care for get? - Can you get Carer&#x27;s Allowance?</title>
        
        <!-- Carer's Allowance stylesheet -->
        <link rel="stylesheet" media="screen" href="<c:url value='/assets/stylesheet/app_t.min.css' />" />
        

        <!-- Print stylesheet -->
        <link href="<c:url value='/assets/stylesheet/print.css' />" media="print" rel="stylesheet" type="text/css">

        <!-- To prevent call to /browserconfig.xml from IE11 and higher. We do not use notifications. -->
        <meta name="msapplication-config" content="none" />

        <link href="<c:url value='/assets/stylesheet/fonts.css' />"  media="all" rel="stylesheet" type="text/css">

        <link rel="shortcut icon" href="<c:url value='/assets/images/favicon-447e4ac1ab790342660eacfe3dcce264.ico' />" type="image/x-icon">
        <link rel="icon"  href="<c:url value='/assets/images/favicon-447e4ac1ab790342660eacfe3dcce264.ico' />" type="image/x-icon">

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <script type="text/javascript">
           var landingURL = "http://www.gov.uk/carers-allowance/how-to-claim"
        </script>
        <script type="text/javascript" src="<c:url value='/assets/javascript/jquery/jquery-1.9.1.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/date.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/bb.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/stageprompt-2.0.1.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/custom.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/respond.min.js' />" />
    </head>
    <body>
        <script type="text/javascript">
            document.body.className = ((document.body.className) ? document.body.className + ' js-enabled' : 'js-enabled');
        </script>
        
        <a href="#main" class="visuallyhidden">Skip to main content</a>            
        <header role="banner" class="global-header" id="global-header">
            <div class="wrapper clearfix">
                <a href="https://www.gov.uk" title="Go to the gov.uk homepage" target="govlink" class="crown">GOV.UK</a>
                <h1 class="heading-medium">Carer's Allowance</h1>
            </div>
        </header>
        <!--end header-->
        
        <div id="global-cookie-message">
            <p>This service uses cookies. 
                <a rel="external"
                   target="_blank" 
                   href="/cookies/en" 
                   onmousedown="trackEvent(&#x27;/allowance/benefits&#x27;,&#x27;Cookies - from banner&#x27;);" 
                   onkeydown="trackEvent(&#x27;/allowance/benefits&#x27;,&#x27;Cookies - from banner&#x27;);">Find out more</a>
            </p>
        </div>
        
        <div id="global-browser-prompt">
            <p>For a safer, faster, better experience online you should upgrade your browser. <a href="https://www.gov.uk/support/browsers">Find out more about browsers</a> <a href="#" class="dismiss" title="Dismiss this message">Close</a></p>
        </div>

        <main class="container" role="main" id="main">            
            <div class="carers-allowance clearfix">
    
                <div class="prototype">
                    <p>This is a prototype. You can't claim Carer's Allowance using this service. <a rel="external" target="_blank" href="https://www.gov.uk/carers-allowance/how-to-claim" target="claimLink">Claim for Carer's Allowance using the live service</a></p>
                </div>
    
                <div class="helper-mobile" id="helper-toggle">
                    <a class="help">Get help</a>
                </div>
            
                <div class="grid-row main-grid">
                    
                    <jsp:include page="../contactDetails.jsp" >
                        <jsp:param name="trackEventPath" value="/allowance/benefits" />
                    </jsp:include>
                    
                    <jsp:include page="./benefits-content.jsp" >
                        <jsp:param name="trackEventPath" value="/allowance/benefits" />
                    </jsp:include>

                </div>        
            </div>
        </main>

        <jsp:include page="../footer.jsp" >
            <jsp:param name="trackEventPath" value="/allowance/benefits" />
        </jsp:include>
        
        <div id="global-app-error" class="app-error hidden"></div>        
    
        <script type="text/javascript" src="<c:url value='/assets/javascript/s_eligibility/answerNoBenefitsMessage.js' />" ></script>
        <script type="text/javascript">
            $(function () {
                window.trackEvent = function(arg1, arg2) {
                    // do nothing
                };
                
                window.initEvents("benefitsAnswer", "NONE");
                trackEvent("times", "claim - eligibility");
                setCookie("claimeligibility",new Date().getTime());                       
                GOVUK.performance.stageprompt.setupForGoogleAnalytics()
            });
        </script>
    </body>
</html>


