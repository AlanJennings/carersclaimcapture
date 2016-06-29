<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="Before you start" nextButtonText="I agree - start my application" backLink="${previousPage}">
        <t:htmlsection><p>When you claim Carer&rsquo;s Allowance the person you care for may stop getting:</p></t:htmlsection>

        <t:htmlsection> 
            <ul class="list-bullet">
                <li>a severe disability premium paid with their benefits, if they get one</li>
                <li>an extra amount for severe disability paid with Pension Credit, if they get one</li>
                <li>reduced Council Tax, if 
                    <a rel="external" 
                       href="https://www.gov.uk/find-your-local-council" 
                       target="_blank" 
                    >their local council</a> 
                    offers it
                </li>
            </ul>
        </t:htmlsection>

        <t:htmlsection>
            <p>Read more about 
                <a rel="external" 
                   href="https://www.gov.uk/carers-allowance/what-youll-get" 
                   target="_blank"
                >how Carer&rsquo;s Allowance affects other benefits</a>.
            </p>
        </t:htmlsection>
    
        <t:htmlsection><p>You <b>must</b> tell the person you care for before you claim.</p></t:htmlsection>
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

</t:mainPage>                