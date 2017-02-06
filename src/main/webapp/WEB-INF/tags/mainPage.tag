<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="page" %>
<%@ attribute name="pageTitle" %>
<%@ attribute name="pageTitleKey" %>
<%@ attribute name="currentPage" %>
<%@ attribute name="analytics" %>   

<%-- TODO can we send analytics from the server side as well? might be able to add useful information --%>
<t:defaultValue value="${pageScope.page}" defaultValue="${requestScope['javax.servlet.forward.servlet_path']}" var="page" />
<t:defaultValue value="${pageScope.currentPage}" defaultValue="${page}" var="currentPage" />
<t:defaultValue value="${pageScope.analytics}" defaultValue="true" var="analytics" />
<t:defaultValue value="${pageScope.pageTitleKey}" defaultValue="${page}.pageTitle" var="pageTitleKey" />
<c:if test="${empty pageTitle}" >
    <c:set var="pageTitle"><t:message parentName="${pageScope.pageTitleKey}" /></c:set>
</c:if>
<%--
<c:if test="${empty pageTitle}" >
    <c:set var="pageTitle"><t:message parentName="${pageScope.page}" element="browserTitle" /></c:set>
</c:if>
--%>
<!DOCTYPE html>

    <!--[if IE 6]>         <html class="ie ie6" lang="en"> <![endif]-->
    <!--[if IE 7]>         <html class="ie ie7" lang="en"> <![endif]-->
    <!--[if IE 8]>         <html class="ie ie8" lang="en"> <![endif]-->
    <!--[if IE 9]>         <html class="ie ie9" lang="en"> <![endif]-->
    <!--[if gt IE 9]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

    <head>
        <!-- current page = <c:out value='${pageScope.currentPage}' /> -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><t:message code="${page}.browserTitle" /></title>

        <!-- Styles for old Internet Explorer Browsers -->
        <!--[if IE 6]><link href="/stylesheet/header-footer-only-ie6.css" media="screen" rel="stylesheet" type="text/css"><![endif]-->
        <!--[if IE 7]><link href="/stylesheet/header-footer-only-ie7.css" media="screen" rel="stylesheet" type="text/css"><![endif]-->
        <!--[if IE 8]><link href="/stylesheet/header-footer-only-ie8.css" media="screen" rel="stylesheet" type="text/css"><![endif]-->

        <!-- Carer's Allowance stylesheet -->
        <link rel="stylesheet" media="screen" href="/stylesheet/app_t.css" />
<style>
    /* See breaks in care etc. */
    .actions button {
        background:none!important;
        border:none; 
        padding:0!important;
        font-family:nta, arial, sans-serif; /*input has OS specific font-family*/
        font-size: 16px;
        color:#069;
        text-decoration:underline;
        cursor:pointer;
        float: right;
    }
    
    .actions button:first-child {
        float: left;
    }
    
    .uppercase {
        text-transform: uppercase!important; 
    }
    
    .lowercase {
        text-transform: lowercase!important; 
    }
</style>

        <!-- Print stylesheet -->
        <link href="/stylesheet/print.css" media="print" rel="stylesheet" type="text/css">

        <!-- To prevent call to /browserconfig.xml from IE11 and higher. We do not use notifications. -->
        <meta name="msapplication-config" content="none" />

        <!--[if IE 8]>
        <script type="text/javascript">
            (function(){if(window.opera){return;}
            setTimeout(function(){var a=document,g,b={families:(g=
            ["nta"]),urls:["<link href='/stylesheet/fonts-ie8.css'" ]},
            c="/javascript/webfont-debug.js",d="script",
            e=a.createElement(d),formData=a.getElementsByTagName(d)[0],h=g.length;WebFontConfig
            ={custom:b},e.src=c,formData.parentNode.insertBefore(e,formData);for(;h=h-1;a.documentElement
            .className+=' wf-'+g[h].replace(/\s/g,'').toLowerCase()+'-n4-loading');},0)
            })()
        </script>

        <![endif]-->

        <!--[if gte IE 9]><!-->
        <link href="/stylesheet/fonts.css" media="all" rel="stylesheet" type="text/css">
        <!--<![endif]-->

        <!--[if lt IE 9]>
        <script src="/javascript/ie.js" type="text/javascript"></script>
        <![endif]-->

        <link rel="shortcut icon" href="/images/favicon-447e4ac1ab790342660eacfe3dcce264.ico" type="image/x-icon">
        <link rel="icon"  href="/images/favicon-447e4ac1ab790342660eacfe3dcce264.ico" type="image/x-icon">

        <!-- For third-generation iPad with high-resolution Retina display: -->
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/images/apple-touch-icon-144x144-4e306e01c31e237722b82b7aa7130082.png">
        <!-- For iPhone with high-resolution Retina display: -->
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/images/apple-touch-icon-114x114-f1d7ccdc7b86d923386b373a9ba5e3db.png">
        <!-- For first- and second-generation iPad: -->
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/images/apple-touch-icon-72x72-2ddbe540853e3ba0d30fbad2a95eab3c.png">
        <!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
        <link rel="apple-touch-icon-precomposed" href="/images/apple-touch-icon-57x57-37527434942ed8407b091aae5feff3f3.png">

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <script type="text/javascript">
           var landingURL = "http://www.gov.uk/carers-allowance/how-to-claim"
        </script>

        <script type="text/javascript" src="/javascript/jquery/jquery-1.9.1.js" ></script>
        <script type="text/javascript" src="/javascript/date.js" ></script>
        <script type="text/javascript" src="/javascript/bb.js" ></script>
        <script type="text/javascript" src="/javascript/stageprompt-2.0.1.js" ></script>
        <script type="text/javascript" src="/javascript/custom.js" ></script>
        <script type="text/javascript" src="/javascript/respond.min.js" ></script>
        <script type="text/javascript" src="/javascript/cleanupHashUrl.js" ></script>

        <!--[if (lt IE 9) & (!IEMobile)]>
        <script src="/javascript/respond.min.js" ></script>
        <![endif]-->

        <c:set var="analyticsId"     value="UA-57523228-19" />
        <c:set var="testAnalyticsId" value="UA-83580544-1" />

        <t:googleAnalyticsInit trackingId="UA-83580544-1" />
    </head>

    <body>
        <script type="text/javascript">
            document.body.className = ((document.body.className) ? document.body.className + ' js-enabled' : 'js-enabled');
        </script>
        
        <a href="#main" class="visuallyhidden">Skip to main content</a>            
        <header role="banner" class="global-header" id="global-header">
            <div class="wrapper clearfix">
                <a href="https://www.gov.uk" title="Go to the gov.uk homepage" target="govlink" class="crown">GOV.UK</a>
                <h1 class="heading-medium"><t:message code="main.page.title" /></h1>
            </div>
        </header>
        <!--end header-->
        
        <div id="global-cookie-message">
            <p><t:message code="cookiePolicy" />
                <a rel="external"
                   target="_blank" 
                   href="/cookies/en" 
                   onmousedown="trackEvent(&#x27;<c:out value='${pageScope.currentPage}' />&#x27;,&#x27;Cookies - from banner&#x27;);" 
                   onkeydown="trackEvent(&#x27;<c:out value='${pageScope.currentPage}' />&#x27;,&#x27;Cookies - from banner&#x27;);"
                ><t:message code="cookiePolicyAnchor" /></a>
            </p>
        </div>
        
        <div id="global-browser-prompt">
            <p>
                <t:message code="latestBrowserUpdate" />
            </p>
        </div>

        
        <jsp:doBody/>
        
        <t:footer currentPage="${pageScope.currentPage}" />
        
        <div id="global-app-error" class="app-error hidden"></div>
        <c:if test="${!empty hash}" >
            <script type="text/javascript">
                $(function(){
                    window.location = '#' + '${hash}_label';
                });
            </script>
        </c:if>
    </body>
</html>

