<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<%--@employmentSummary(job:Iteration) = @{--%>
    <%--val jobDetails = job.questionGroup[JobDetails].get--%>
    <%--val startDate = jobDetails.jobStartDate--%>
    <%--val lastWorkDate = jobDetails.lastWorkDate--%>
    <%--val frequency = job.questionGroup[LastWage].get.oftenGetPaid--%>
    <%--val pay = job.questionGroup[LastWage].get.grossPay--%>

    <%--Html(s"<b>${jobDetails.employerName}</b><br/>${employmentDates(startDate, lastWorkDate)}${paymentWithCurrency(pay)} ${employmentPaymentFrequency(frequency)} ${includeExpenses(job)}<br/>")--%>
<%--}--%>
<%--@employmentDates(startDate: Option[DayMonthYear], endDate: Option[DayMonthYear]) = @{--%>
    <%--(startDate != None) match {--%>
        <%--case true if (endDate != None) => s"${messages("employmentFrom")} ${startDate.get.`dd/MM/yyyy`} ${messages("employmentTo")} ${endDate.get.`dd/MM/yyyy`}<br/>"--%>
        <%--case true => s"${messages("employmentFrom")} ${startDate.get.`dd/MM/yyyy`}<br/>"--%>
        <%--case false if (endDate != None) => s"${messages("employmentFinished")} ${endDate.get.`dd/MM/yyyy`}<br/>"--%>
        <%--case _ => ""--%>
    <%--}--%>
<%--}--%>
<%--@includeExpenses(job:Iteration) = @{--%>
    <%--val pensionAndExpenses = job.questionGroup[PensionAndExpenses].get--%>
    <%--if (pensionAndExpenses.payPensionScheme.answer == Mappings.yes) messages("includingExpenses")--%>
    <%--else ""--%>
<%--}--%>
<%--@paymentWithCurrency(grossPay: String) = @{--%>
    <%--if (grossPay startsWith "£")grossPay--%>
    <%--else s"£$grossPay"--%>
<%--}--%>
<%--@employmentPaymentFrequency(payFrequency: PaymentFrequency) = @{--%>
    <%--if (payFrequency.frequency == Weekly) messages("employmentWeekly")--%>
    <%--else if(payFrequency.frequency == Fortnightly)messages("employmentFortnightly")--%>
    <%--else if(payFrequency.frequency == FourWeekly)messages("employmentFourWeekly")--%>
    <%--else if(payFrequency.frequency == Monthly)messages("employmentMonthly")--%>
    <%--else messages("employmentOther")--%>
<%--}--%>
<%--@jobs = @{claim.questionGroup[Jobs]}--%>
<%--@jobsId = @{ "employment_jobs" }--%>
<%--@elementLinkedToId = @{--%>
    <%--"?hash=jobs&returnToSummary=" + jobsId--%>
<%--}--%>
<%--@if(jobs.isDefined && jobs.get.jobs.size > 0){--%>
    <%--<tr class="review-change" id="@jobsId">--%>
        <%--<td id="@getLabelId(jobsId, false)" class="review-label">--%>
            <%--<b>@messages("jobs")</b>--%>
        <%--</td>--%>
        <%--<td id="@getValueId(jobsId, false)" class="review-value">--%>
            <%--<ul>--%>
            <%--@for(job <- jobs.get.jobs) {--%>
                <%--<li>@employmentSummary(job)</li>--%>
            <%--}--%>
            <%--</ul>--%>
        <%--</td>--%>
        <%--<td id="@getLinkContainerId(jobsId, false)" class="review-action hide-print">--%>
        <%--@if(!disableChangeButton) {--%>
            <%--<a class="secondary" href="@controllers.preview.routes.Preview.redirect(jobsId)@elementLinkedToId">--%>
            <%--@Html(messages("preview.change"))--%>
            <%--</a>--%>
        <%--}--%>
        <%--</td>--%>
    <%--</tr>--%>
<%--}--%>
