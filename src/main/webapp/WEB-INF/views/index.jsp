<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<c:set var="baseRef" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${pageContext.request.contextPath}/" />

<html>
    <head>
        <style>
            body {
                font-family: Arial, sans-serif;
                font-size: 16px;    
                padding: 0;
            }

            .sections {
                margin-left: 25px;
                width: 600px;
            }
            
            .section-name {
                margin-top: 20px;
                margin-bottom: 0px;
                text-transform: capitalize;
            }

            ul {
                margin: 0;
                padding: 0;
            }

            li {
                list-style: none;
                padding-left: 25px;
                padding-top:3px;
                padding-bottom: 3px;
            }
            
            .unavailable {
                background-color: #F57777;
            }
        </style>
    </head>
    <body>
        <h2>Index page, this should redirect to /allowance/benefits</h2>
        <div class="sections">
            <div class="section">
                <h2 class="section-name">allowance</h2>
                <ul>
                    <li><a href="${baseRef}allowance/benefits">benefits</a> (/allowance/benefits)</li>
                    <li><a href="${baseRef}allowance/eligibility">eligibility</a> (/allowance/eligibility)</li>
                    <li><a href="${baseRef}allowance/approve">approve</a> (/allowance/approve)</li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">disclaimer</h2>
                <ul>
                    <li><a href="${baseRef}disclaimer/disclaimer">disclaimer</a> (/disclaimer/disclaimer)</li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">third-party</h2>
                <ul>
                    <li><a href="${baseRef}third-party/third-party">third-party</a> (/third-party/third-party)</li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">your-claim-date</h2>
                <ul>
                    <li><a href="${baseRef}your-claim-date/claim-date">claim-date</a> (/your-claim-date/claim-date)</li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">about-you</h2>
                <ul>
                    <li><a href="${baseRef}about-you/your-details">your-details</a> (/about-you/your-details)</li>
                    <li><a href="${baseRef}about-you/marital-status">marital-status</a></li>
                    <li><a href="${baseRef}about-you/contact-details">contact-details</a></li>
                    <li><a href="${baseRef}about-you/nationality-and-residency">nationality-and-residency</a></li>
                    <li><a href="${baseRef}about-you/other-eea-state-or-switzerland">other-eea-state-or-switzerland</a></li>            
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">your-partner</h2>
                <ul>
                    <li><a href="${baseRef}your-partner/personal-details">personal-details</a></li>            
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">care-you-provide</h2>
                <ul>
                    <li><a href="${baseRef}care-you-provide/their-personal-details">their-personal-details</a></li>
                    <li><a href="${baseRef}care-you-provide/more-about-the-care">more-about-the-care</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">breaks</h2>
                <ul>
                    <li><a href="${baseRef}breaks/breaks-in-care">breaks-in-care</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">education</h2>
                <ul>
                    <li><a href="${baseRef}education/your-course-details">your-course-details</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">your-income</h2>
                <ul>
                    <li><a href="${baseRef}your-income/your-income">your-income</a></li>
                    <li><a href="${baseRef}your-income/employment/been-employed">employment/been-employed</a></li>
                    <li><a href="${baseRef}your-income/self-employment/self-employment-dates">self-employment/self-employment-dates</a></li>
                    <li><a href="${baseRef}your-income/self-employment/pensions-and-expenses">self-employment/pensions-and-expenses</a></li>
                    <li><a href="${baseRef}your-income/employment/additional-info">employment/additional-info</a></li>
                    <li><a href="${baseRef}your-income/statutory-sick-pay">statutory-sick-pay</a></li>
                    <li><a href="${baseRef}your-income/smp-spa-sap">smp-spa-sap</a></li>
                    <li><a href="${baseRef}your-income/fostering-allowance">fostering-allowance</a></li>
                    <li><a href="${baseRef}your-income/direct-payment">direct-payment</a></li>
                    <li><a href="${baseRef}your-income/other-income">other-income</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">pay-details</h2>
                <ul>
                    <li><a href="${baseRef}pay-details/how-we-pay-you">how-we-pay-you</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">information</h2>
                <ul>
                    <li><a href="${baseRef}information/additional-info">additional-info</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">preview</h2>
                <ul>
                    <li><a href="${baseRef}preview">preview</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">consent-and-declaration</h2>
                <ul>
                    <li><a href="${baseRef}consent-and-declaration/declaration">declaration</a></li>
                </ul>
            </div>
    
            <div class="section">
                <h2 class="section-name">thankyou-title</h2>
                <ul>
                    <li><a href="${baseRef}thankyou/apply-carers">apply-carers</a></li>
                </ul>
            </div>
        </div>
    </body>
</html>
