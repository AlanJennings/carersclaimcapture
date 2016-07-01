<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">
        <main class="container" role="main" id="main">
            <div class="carers-allowance clearfix">
<%--    
                <section class="js-message">
                    <h2>Javascript is currently disabled</h2>
                    <p>For full functionality of this website it is necessary to enable JavaScript, not doing so will result in a reduced user experience.</p>
                </section>
--%>            
                <article class="column-three-quarters main-content finish-statement">
                    <div style="height: 50px;" ></div>
        
                    <section class="app-complete">
                        <h1 class="form-title heading-large compound">Your claim has been sent</h1>
                        <p class="nino-title">Your reference is your National Insurance number.</p>
                    </section>
        
                    <p class="hide-desktops">What did you think of this service?</p>
        
                    <div class="finish-info">
                        <h2 class="heading-medium">What happens next</h2>
                            <p>You don't have to do anything else - you'll usually get a decision within 4 weeks.</p>
                    </div>
        
                    <br>
        
                    <p><a rel="external" href="https://www.gov.uk/carers-allowance-report-change" target="_blank">Report a change in your circumstances.</a></p>
        
                    <div class="finish-button">
                        <a href="https://www.gov.uk/done/apply-carers-allowance" 
                           rel="external" 
                           target="_blank" 
                           class="secondary" 
                           onmousedown="trackEvent('/thankyou/apply-carers','Feedback');" 
                           onkeydown="trackEvent('/thankyou/apply-carers','Feedback');"
                        >What did you think of this service?</a>
                        (Takes 30 seconds.)
                    </div>
                    <div style="height: 50px;" ></div>
                </article>                
            </div>
        </main>
</t:mainPage>
