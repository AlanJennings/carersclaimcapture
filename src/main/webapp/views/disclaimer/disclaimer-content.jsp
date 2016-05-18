<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<article class="column-three-quarters main-content">
    <nav class="back-nav" role="navigation">
        <a id="topBackButton" name="topBackButton" href="/allowance/approve">Back</a>
    </nav>

    <h1 class="form-title heading-large">Before you start</h1>

    <form action="/disclaimer/disclaimer" method="POST" role="form">    
        <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80"/>
        
        <fieldset class="form-elements">
            <div class="declaration">
                <p>When you claim Carer's Allowance the person you care for may stop getting:</p>

                <ul class="list-bullet">
                    <li>a severe disability premium paid with their benefits, if they get one</li>
                    <li>an extra amount for severe disability paid with Pension Credit, if they get one</li>
                    <li>reduced Council Tax, if 
                        <a rel="external" 
                           href="https://www.gov.uk/find-your-local-council" 
                           target="_blank" 
                           onmousedown="trackEvent(&#x27;/disclaimer/disclaimer&#x27;,&#x27;Disclaimer - Find your local council&#x27;);" 
                           onkeydown="trackEvent(&#x27;/disclaimer/disclaimer&#x27;,&#x27;Disclaimer - Find your local council&#x27;);"
                        >their local council</a> 
                        offers it
                    </li>
                </ul>
                
                <p>Read more about 
                    <a rel="external" 
                       href="https://www.gov.uk/carers-allowance/what-youll-get" 
                       target="_blank" 
                       onmousedown="trackEvent(&#x27;/disclaimer/disclaimer&#x27;,&#x27;Disclaimer - How it affects benefits&#x27;);" 
                       onkeydown="trackEvent(&#x27;/disclaimer/disclaimer&#x27;,&#x27;Disclaimer - How it affects benefits&#x27;);"
                    >how Carer's Allowance affects other benefits</a>.
                </p>
                <p>
                    You <b>must</b> tell the person you care for before you claim.
                </p>
                
            </div>
        </fieldset>

        <nav class="form-steps" role="navigation">
            <ul>
                <li>
                    <button type="submit" 
                            id="next" 
                            name="read" 
                            value="yes" 
                            class="button"  
                            onmousedown="trackEvent(&#x27;/disclaimer/disclaimer&#x27;,&#x27;Disclaimer - I agree - start my application&#x27;);" 
                            onkeydown="trackEvent(&#x27;/disclaimer/disclaimer&#x27;,&#x27;Disclaimer - I agree - start my application&#x27;);"
                    >I agree - start my application</button>
                </li>

                <li>
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
                </li>

                <li>
                    <a id="bottomBackButton" name="bottomBackButton" href="/allowance/approve">Back</a>
                </li>
            </ul>
        </nav>
    </form>
</article>
                