<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:mainPage pageTitle="${pageTitle}" currentPage="${currentPage}">

    <t:pageContent errors="${validationErrors}" pageTitle="About you - the carer" section="Section 3 of 11" backLink="${previousPage}">
        <t:radiobuttons id="maritalStatus" 
                        name="maritalStatus" 
                        optionValues="Married or civil partner|Single|Divorced or civil partnership dissolved|Widowed or surviving civil partner|Separated|Living with partner"
                        optionLabels="Married or civil partner|
                                      Single|
                                      Divorced or civil partnership dissolved|
                                      Widowed or surviving civil partner|
                                      Separated|
                                      Living with partner"
                        value="${maritalStatus}"
                        label="Your status" 
                        errors="${validationErrors}" 
        />
    
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
