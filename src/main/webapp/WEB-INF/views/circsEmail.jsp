<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<!--[if IE 7]>         <html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8]>         <html class="ie ie8" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<head>
    <title>GOV.UK</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <style>
        html, body, button, input, table, td, th {
            font-family: Arial, sans-serif;
        }

        .header {
            background: black;
            padding: 10px;
            font-weight: bold;
        }

        .logo img,
        .logo div {
            display: inline-block;
            vertical-align: middle;
        }

        .logo img {
            width: 35px;
            height: 31px;
            margin-top: -1px;
            margin-right: 5px;
        }

        .logo div {
            font-size: 30px;
            color: white;
            text-decoration: none;
        }

    </style>
</head>

<body class="email-template">
<div class="header" style="background: black; padding: 10px; font-weight: bold;">
    <div class="wrapper logo">
        <font face="arial, sans-serif">
            <img width="35" height="31" src="https://www.carersallowance.service.gov.uk/images/crown.png" style="display: inline-block; vertical-align: middle; margin-right: 5px;" />
            <div class="crown" style="display: inline-block; vertical-align: middle; font-size: 30px; color: white; text-decoration: none;">
                GOV&#8203;.&#8203;UK
            </div>
        </font>
    </div>
</div>

<div class="main">
    <font face="arial, sans-serif">
        <br />
        <h1 class="form-title heading-large"><t:message code="mail.claim.title" /></h1>
        <p><t:message code="mail.claim.successful" /></p>
        <div class="section">
            <h2><t:message code="evidenceText.section1.line1" /></h2>
            <c:if test="${isCofcFinishedEmployment || !isCofcSelfEmployment}" >
                <p><t:message code="mail.next.send1" /></p>
                <ul>
                    <c:if test="${!isCofcSelfEmployment}" >
                        <li><t:message code="evidence.email.cofc.employment.anyPayslips" /></li>
                        <c:if test="${isCofcFinishedEmployment}" >
                            <li><t:message code="evidence.email.cofc.employment.p45" /></li>
                        </c:if>
                        <li><t:message code="evidence.pensionStatements" /></li>
                    </c:if>
                </ul>

                <p><t:message code="evidence.include.documents" /></p>

                <p><t:message code="evidenceText.section4.line9" /></p>

                <div class="address">
                    <span><b><t:message code="mail.next.send6" /></b></span><br />
                    <span><t:message code="mail.next.send7" /></span><br />
                    <span><t:message code="mail.next.send8" /></span><br />
                    <span><t:message code="mail.next.send9" /></span><br />
                    <c:if test="${!isOriginGB}" >
                        <span><t:message code="mail.next.send10" /></span> <br />
                    </c:if>
                </div>

            </c:if>

            <c:choose>
                <c:when test="${isCofcFinishedEmployment || !isCofcSelfEmployment}">
                    <p><t:message code="mail.next.line2" /></p>
                </c:when>
                <c:otherwise>
                    <p><t:message code="mail.next.line" /></p>
                </c:otherwise>
            </c:choose>

        </div>

        <div class="section">
            <h2><t:message code="mail.next.line3" /></h2>

            <p><t:message code="mail.next.line4" /></p>
            <p><t:message code="mail.next.line5" /></p>
            <p><t:message code="mail.next.line6" /></p>

            <div class="address">
                <span><t:message code="mail.next.line9" /></span><br />
                <span><t:message code="mail.next.line10" /></span><br />
                <span><t:message code="mail.next.line11" /></span><br />
                <span><t:message code="mail.next.line12" /></span><br />
                <c:if test="${!isOriginGB}" >
                    <span><t:message code="mail.next.line15" /></span> <br />
                </c:if>
                <span> <t:message code="mail.next.line13" /></span>
            </div>
            <p><b><t:message code="mail.next.line14" /></b></p>
        </div>

        <div class="section">
            <h2><t:message code="mail.disclaimer.title" /></h2>

            <p><t:message code="mail.disclaimer.line1" /></p>
        </div>

    </font>

</div><!-- main -->

<div class="footer">
    <p><b><t:message code="mail.donotreply" /></b></p>
    <br />
    <p style="font-size: 12px; color: #BFC1C3;">${versionSchemaTransactionInfo}</p>
</div>
</body>
</html>
