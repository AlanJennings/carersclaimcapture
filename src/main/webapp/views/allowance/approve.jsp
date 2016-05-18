<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <!-- current page = <c:out value='${currentPage}' /> -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Can you get Carer&#x27;s Allowance?</title>

        <!-- Carer's Allowance stylesheet -->
        <link rel="stylesheet" media="screen" href="<c:url value='/assets/stylesheet/app_t.min.css' />" />

        <!-- Print stylesheet -->
        <link href="<c:url value='/assets/stylesheet/print.css' />" media="print" rel="stylesheet" type="text/css">

        <!-- To prevent call to /browserconfig.xml from IE11 and higher. We do not use notifications. -->
        <meta name="msapplication-config" content="none" />

        <!--[if IE 8]>
        <script type="text/javascript">
            (function(){if(window.opera){return;}
            setTimeout(function(){var a=document,g,b={families:(g=
            ["nta"]),urls:["<link href="<c:url value='/assets/stylesheet/fonts-ie8.css' />" ]},
            c="<c:url value='/assets/javascript/webfont-debug.js' />",d="script",
            e=a.createElement(d),formData=a.getElementsByTagName(d)[0],h=g.length;WebFontConfig
            ={custom:b},e.src=c,formData.parentNode.insertBefore(e,formData);for(;h=h-1;a.documentElement
            .className+=' wf-'+g[h].replace(/\s/g,'').toLowerCase()+'-n4-loading');},0)
            })()
        </script>

        <![endif]-->

        <!--[if gte IE 9]><!-->
        <link href="<c:url value='/assets/stylesheet/fonts.css' />" media="all" rel="stylesheet" type="text/css">
        <!--<![endif]-->

        <!--[if lt IE 9]>
        <script src="<c:url value='/assets/javascript/ie.js' />" type="text/javascript"></script>
        <![endif]-->

        <link href="<c:url value='/assets/stylesheet/fonts.css' />"  media="all" rel="stylesheet" type="text/css">

        <link rel="shortcut icon" href="<c:url value='/assets/images/favicon-447e4ac1ab790342660eacfe3dcce264.ico' />" type="image/x-icon">
        <link rel="icon"  href="<c:url value='/assets/images/favicon-447e4ac1ab790342660eacfe3dcce264.ico' />" type="image/x-icon">

        <!-- For third-generation iPad with high-resolution Retina display: -->
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value='/assets/images/apple-touch-icon-144x144-4e306e01c31e237722b82b7aa7130082.png' />">
        <!-- For iPhone with high-resolution Retina display: -->
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value='/assets/images/apple-touch-icon-114x114-f1d7ccdc7b86d923386b373a9ba5e3db.png' />">
        <!-- For first- and second-generation iPad: -->
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value='/assets/images/apple-touch-icon-72x72-2ddbe540853e3ba0d30fbad2a95eab3c.png' />">
        <!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
        <link rel="apple-touch-icon-precomposed" href="<c:url value='/assets/images/apple-touch-icon-57x57-37527434942ed8407b091aae5feff3f3.png' />">

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <script type="text/javascript">
           var landingURL = "http://www.gov.uk/carers-allowance/how-to-claim"
        </script>
        <script type="text/javascript" src="<c:url value='/assets/javascript/jquery/jquery-1.9.1.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/date.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/bb.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/stageprompt-2.0.1.js' />" />
        <script type="text/javascript" src="<c:url value='/assets/javascript/custom.js' />" />

        <!--[if (lt IE 9) & (!IEMobile)]>
        <script src="<c:url value='/assets/javascript/respond.min.js' />" ></script>
        <![endif]-->
        
        <script>
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
            
            ga('create', 'UA-57523228-1', {
              
              'siteSpeedSampleRate': '100'
            
            });
            ga('send', 'pageview');
            ga('set', 'anonymizeIp', true);
            ga('set', 'nonInteraction', true);
        </script>
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
                   onmousedown="trackEvent(&#x27;<c:out value='${currentPage}' />&#x27;,&#x27;Cookies - from banner&#x27;);" 
                   onkeydown="trackEvent(&#x27;<c:out value='${currentPage}' />&#x27;,&#x27;Cookies - from banner&#x27;);"
                >Find out more</a>
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
                    
                    <jsp:include page="./approve-content.jsp" >
                        <jsp:param name="currentPage" value="<c:out value='${currentPage}' />" />
                    </jsp:include>

                </div>
            </div>
        </main>

        <jsp:include page="../footer.jsp" >
            <jsp:param name="currentPage" value="<c:out value='${currentPage}' />" />
        </jsp:include>
   
        <div id="global-app-error" class="app-error hidden"></div>
      
      </body>
</html>
