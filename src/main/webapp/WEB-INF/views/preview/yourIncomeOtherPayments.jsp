<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cads" uri="http://uk.gov.dwp.carersallowance/functions" %>

<%--@yourIncome = @{claim.questionGroup[YourIncomes].getOrElse(YourIncomes())}--%>
<%--@yourIncomeStatutorySickPayQ = @{claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay())}--%>
<%--@yourIncomeStatutoryPayQ = @{claim.questionGroup[StatutoryMaternityPaternityAdoptionPay].getOrElse(StatutoryMaternityPaternityAdoptionPay())}--%>
<%--@yourIncomeFosteringAllowanceQ = @{claim.questionGroup[FosteringAllowance].getOrElse(FosteringAllowance())}--%>
<%--@yourIncomeDirectPaymentQ = @{claim.questionGroup[DirectPayment].getOrElse(DirectPayment())}--%>
<%--@yourIncomeRentalIncomeQ = @{claim.questionGroup[RentalIncome].getOrElse(RentalIncome())}--%>
<%--@yourIncomeOtherPaymentsQ = @{claim.questionGroup[OtherPayments].getOrElse(OtherPayments())}--%>
<%--@claimDate = @{claim.questionGroup[ClaimDate].getOrElse(ClaimDate())}--%>
<%--@hasYourIncomeOtherPayment(value: Option[String]) = @{value == Some("true")}--%>

<%--@yourIncomeAllOtherPayments = @{--%>
    <%--displayMessage(yourIncome.yourIncome_none, "preview.yourIncome.none") +--%>
            <%--displayMessage(yourIncome.yourIncome_sickpay, "preview.yourIncome.ssp") +--%>
            <%--displayMessage(yourIncome.yourIncome_patmatadoppay, "preview.yourIncome.spmp") +--%>
            <%--displayMessage(yourIncome.yourIncome_fostering, "preview.yourIncome.fostering") +--%>
            <%--displayMessage(yourIncome.yourIncome_directpay, "preview.yourIncome.direct") +--%>
            <%--displayMessage(yourIncome.yourIncome_rentalincome, "preview.yourIncome.rental") +--%>
            <%--displayMessage(yourIncome.yourIncome_anyother, "preview.yourIncome.anyother")--%>
<%--}--%>
<%--@yourIncomeStatutorySickPay = @{--%>
    <%--displayTitleMessage("preview.yourIncome.ssp") +--%>
            <%--displayMessageDetail(yourIncomeStatutorySickPayQ.whoPaidYouThisPay) +--%>
            <%--displayPaymentFrequencyMessage(yourIncomeStatutorySickPayQ.amountOfThisPay, yourIncomeStatutorySickPayQ.howOftenPaidThisPay, yourIncomeStatutorySickPayQ.howOftenPaidThisPayOther) +--%>
            <%--displayStillInPaymentOrEnded(yourIncomeStatutorySickPayQ.whenDidYouLastGetPaid, "preview.yourIncome.stillBeingPaid", "preview.yourIncome.ended")--%>
<%--}--%>
<%--@yourIncomeStatutoryPay = @{--%>
    <%--displayTitleMessage("preview.yourIncome.spmp") +--%>
            <%--displayMessageDetail(yourIncomeStatutoryPayQ.whoPaidYouThisPay) +--%>
            <%--displayPaymentFrequencyMessage(yourIncomeStatutoryPayQ.amountOfThisPay, yourIncomeStatutoryPayQ.howOftenPaidThisPay, yourIncomeStatutoryPayQ.howOftenPaidThisPayOther) +--%>
            <%--displayStillInPaymentOrEnded(yourIncomeStatutoryPayQ.whenDidYouLastGetPaid, "preview.yourIncome.stillBeingPaid", "preview.yourIncome.ended")--%>
<%--}--%>
<%--@yourIncomeFosteringAllowance = @{--%>
    <%--displayTitleMessage("preview.yourIncome.fostering") +--%>
            <%--displayFosteringAllowanceValue(yourIncomeFosteringAllowanceQ.paymentTypesForThisPay, yourIncomeFosteringAllowanceQ.paymentTypesForThisPayOther, "preview.yourIncome.paidBy") +--%>
            <%--displayPaymentFrequencyMessage(yourIncomeFosteringAllowanceQ.amountOfThisPay, yourIncomeFosteringAllowanceQ.howOftenPaidThisPay, yourIncomeFosteringAllowanceQ.howOftenPaidThisPayOther) +--%>
            <%--displayStillInPaymentOrEnded(yourIncomeFosteringAllowanceQ.whenDidYouLastGetPaid, "preview.yourIncome.stillBeingPaid", "preview.yourIncome.ended")--%>
<%--}--%>
<%--@yourIncomeDirectPayment = @{--%>
    <%--displayTitleMessage("preview.yourIncome.direct") +--%>
            <%--displayMessageDetail(yourIncomeDirectPaymentQ.whoPaidYouThisPay) +--%>
            <%--displayPaymentFrequencyMessage(yourIncomeDirectPaymentQ.amountOfThisPay, yourIncomeDirectPaymentQ.howOftenPaidThisPay, yourIncomeDirectPaymentQ.howOftenPaidThisPayOther) +--%>
            <%--displayStillInPaymentOrEnded(yourIncomeDirectPaymentQ.whenDidYouLastGetPaid, "preview.yourIncome.stillBeingPaid", "preview.yourIncome.ended")--%>
<%--}--%>
<%--@yourIncomeRentalIncome = @{--%>
    <%--displayTitleMessage("preview.yourIncome.rental") +--%>
            <%--displayMessageOnly(yourIncomeRentalIncomeQ.rentalIncomeInfo, "preview.yourIncome.detailsProvided")--%>
<%--}--%>
<%--@yourIncomeOtherPayments = @{--%>
    <%--displayTitleMessage("preview.yourIncome.anyother") +--%>
            <%--displayMessageOnly(yourIncomeOtherPaymentsQ.otherPaymentsInfo, "preview.yourIncome.detailsProvided")--%>
<%--}--%>
<%--@displayMessageOnly(value: String, message: String) = @{--%>
    <%--if (value == "") ""--%>
    <%--else s"${messages(message)}<br/>"--%>
<%--}--%>
<%--@displayMessage(value: Option[String], message: String) = @{--%>
    <%--if (value == None) ""--%>
    <%--else s"${messages(message)}<br/>"--%>
<%--}--%>
<%--@displayMessageDetail(value: String) = @{--%>
    <%--s"$value<br/>"--%>
<%--}--%>
<%--@paymentFrequencyLabel(frequency: String) = @{--%>
    <%--frequency match{--%>
        <%--case "Weekly" => messages("incomeWeekly")--%>
        <%--case "Fortnightly" => messages("incomeFortnightly")--%>
        <%--case "Four-Weekly" => messages("incomeFourWeekly")--%>
        <%--case "Monthly" => messages("incomeMonthly")--%>
        <%--case _ => messages("incomeOther")--%>
    <%--}--%>
<%--}--%>
<%--@displayPaymentFrequencyMessage(paymentValue: String, frequencyValue: String, frequencyValueOther: Option[String]) = @{--%>
    <%--s"$paymentValue ${if (frequencyValueOther.isDefined) frequencyValueOther.get else paymentFrequencyLabel(frequencyValue) }<br/>"--%>
<%--}--%>
<%--@displayTitleMessage(message: String) = @{--%>
    <%--s"<b>${messages(message)}</b><br/>"--%>
<%--}--%>
<%--@displayStillInPaymentOrEnded(date: Option[DayMonthYear], stillBeingPaidMessage: String, endedMessage: String) = @{--%>
    <%--if (date.isDefined) s"${messages(endedMessage)} ${date.get.`d month yyyy`}<br/>"--%>
    <%--else s"${messages(stillBeingPaidMessage)}<br/>"--%>
<%--}--%>
<%--@displayFosteringAllowanceValue(value: String, otherValue: Option[String], preText: String) = @{--%>
    <%--if (otherValue.isDefined) s"${messages(preText)} ${otherValue.get}<br/>"--%>
    <%--else s"${messages(preText)} ${messages(value)}<br/>"--%>
<%--}--%>
<%--@disableChangeButton = @{!getBooleanProperty("cyaToggleVisible")}--%>

<%--@fieldWithLink("your_income", messages("yourIncome", claimDate.dateOfClaim.`d month yyyy`), yourIncomeAllOtherPayments, disableLink = disableChangeButton, elementId = Some("yourIncome"))--%>
<%--@if(hasYourIncomeOtherPayment(yourIncome.yourIncome_sickpay)){
@fieldWithLink("your_income_statutory_sick_pay", "", yourIncomeStatutorySickPay, disableLink = disableChangeButton, elementId = Some("stillBeingPaidThisPay"))
}--%>
<%--@if(hasYourIncomeOtherPayment(yourIncome.yourIncome_patmatadoppay)){
@fieldWithLink("your_income_statutory_pay", "", yourIncomeStatutoryPay, disableLink = disableChangeButton, elementId = Some("paymentTypesForThisPay"))
}--%>
<%--@if(hasYourIncomeOtherPayment(yourIncome.yourIncome_fostering)){
@fieldWithLink("your_income_fostering_allowance", "", yourIncomeFosteringAllowance, disableLink = disableChangeButton, elementId = Some("fosteringAllowancePay"))
}--%>
<%--@if(hasYourIncomeOtherPayment(yourIncome.yourIncome_directpay)){
@fieldWithLink("your_income_direct_payment", "", yourIncomeDirectPayment, disableLink = disableChangeButton, elementId = Some("stillBeingPaidThisPay_directPayment"))
}--%>
<%--@if(hasYourIncomeOtherPayment(yourIncome.yourIncome_rentalincome)){
@fieldWithLink("your_income_rental_income", "", yourIncomeRentalIncome, disableLink = disableChangeButton, elementId = Some("rentalIncomeInfo"))
}--%>
<%--@if(hasYourIncomeOtherPayment(yourIncome.yourIncome_anyother)){
@fieldWithLink("your_income_other_payments", "", yourIncomeOtherPayments, disableLink = disableChangeButton, elementId = Some("otherPaymentsInfo"))
}--%>

