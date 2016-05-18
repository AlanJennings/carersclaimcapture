<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<article class="column-three-quarters main-content">
    <nav class="back-nav" role="navigation">
        <a id="topBackButton" name="topBackButton" href="/your-claim-date/claim-date">Back</a>
    </nav>

    <h1 class="form-title heading-large">
        <span class="section-progress"> Section 1 of 11 </span> 
        Are you applying for Carer's Allowance for yourself?
    </h1>

    <form action="/third-party/third-party" method="POST" role="form">

        <input type="hidden" name="csrfToken" value="ac56313b00f29fa2952b1e80" />

        <fieldset class="form-elements">
            <ul>
                <li class="form-group ">
                    <fieldset>
                        <legend id="thirdParty_questionLabel" class="form-label-bold "></legend>
                        <ul class="form-group form-group-compound" id="thirdParty">
                            <li>
                                <label class="block-label"
                                       for="thirdParty_yesCarer"
                                       onmousedown="trackEvent(&#x27;/third-party/third-party&#x27;,&#x27;thirdParty.tracking&#x27;,&quot;Yes, you&#x27;re the carer&quot;);">
                                    <input type="radio"
                                           id="thirdParty_yesCarer"
                                           name="thirdParty"
                                           onmousedown="trackEvent(&#x27;/third-party/third-party&#x27;,&#x27;thirdParty.tracking&#x27;,&quot;Yes, you&#x27;re the carer&quot;);"
                                           value="yesCarer" /> 
                                    <span>Yes, you&#x27;re the carer</span>
                                </label>
                            </li>

                            <li>
                                <label class="block-label"
                                       for="thirdParty_noCarer"
                                       onmousedown="trackEvent(&#x27;/third-party/third-party&#x27;,&#x27;thirdParty.tracking&#x27;,&quot;No, you&#x27;re applying on behalf of the carer&quot;);">
                                    <input type="radio"
                                           id="thirdParty_noCarer"
                                           name="thirdParty"
                                           onmousedown="trackEvent(&#x27;/third-party/third-party&#x27;,&#x27;thirdParty.tracking&#x27;,&quot;No, you&#x27;re applying on behalf of the carer&quot;);"
                                           value="noCarer" checked />
                                    <span>No, you&#x27;re applying on behalf of the carer</span>
                                </label>
                            </li>
                        </ul>
                    </fieldset>
                </li>
                
                <li id="thirdPartyWrap" class="form-group">
                    <ul class="extra-group">
                        <li class="form-group ">
                            <label id="thirdParty_nameAndOrganisation_questionLabel"
                                   class="form-label-bold"
                                   for="thirdParty_nameAndOrganisation">Your name and organisation </label>
                                
                            <input type="text"
                                   class="form-control "
                                   id="thirdParty_nameAndOrganisation"
                                   name="thirdParty.nameAndOrganisation"
                                   value="Jenny Bloggs Preston carers"
                                   maxLength="60"
                                   autocomplete="off">

                            <p class="form-hint">
                                Fill the rest of the form in as if you're the carer. For
                                example, if asked for 'your address' put the address of
                                the person doing the caring.
                            </p>
                        </li>
                    </ul>
                </li>
            </ul>
        </fieldset>
        
        <nav class="form-steps" role="navigation">
            <ul>
                <li>
                    <button type="submit" id="next" name="action" value="next" class="button">Next</button>
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
                    <a id="bottomBackButton" name="bottomBackButton" href="/your-claim-date/claim-date">Back</a>
                </li>
            </ul>
        </nav>
    </form>

    <script type="text/javascript" src="/assets/javascript/third_party/thirdParty.js"></script>
    <script type="text/javascript">
        $(function () {
            trackEvent ( "times", "claim - start" );
            setCookie("claimstart", new Date().getTime());
            
        });
        $(function(){
            window.initEvents("thirdParty_yesCarer", "thirdParty_noCarer", "thirdParty_nameAndOrganisation")
            trackEvent ( "times", "claim - start" );
            setCookie("claimstart", new Date().getTime());
        
        });
    </script>
</article>
                