package uk.gov.dwp.carersallowance.controller.preview;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.C3Constants;

import javax.inject.Inject;

/**
 * Created by peterwhitehead on 18/01/2017.
 */
@Component
public class PreviewPageProcessing {
    private final PreviewUtils previewUtils;
    private final Boolean displayChangeButton;
    private final Integer hidePayDetails;
    private final Boolean saveForLaterSaveEnabled;
    private final String saveForLaterUrl;
    private final String originTag;
    
    @Inject
    public PreviewPageProcessing(@Value("${preview.display.change.button}") final Boolean displayChangeButton,
                                 @Value("${age.hide.paydetails}") final Integer hidePayDetails,
                                 @Value("${save.for.later.save.enabled}") final Boolean saveForLaterSaveEnabled,
                                 @Value("${save.for.later.url}") final String saveForLaterUrl,
                                 @Value("${origin.tag}") final String originTag,
                                 final PreviewUtils previewUtils) {
        this.previewUtils = previewUtils;
        this.displayChangeButton = displayChangeButton;
        this.hidePayDetails = hidePayDetails;
        this.saveForLaterSaveEnabled = saveForLaterSaveEnabled;
        this.saveForLaterUrl = saveForLaterUrl;
        this.originTag = originTag;
    }
    
    public void createParametersForPreviewPage(final Model model, final Session session) {
        displayParametersForPreviewPage(model, session);
        displayParametersForAboutYouPage(model, session);
        displayParametersForNationalityPage(model, session);
        displayParametersForPartnerDetailsPage(model, session);
        displayParametersForCareYouProvidePage(model, session);
        displayParametersForBreaksInCarePage(model, session);
        displayParametersForEductionPage(model, session);
        displayParametersForBankDetailsPage(model, session);
        displayParametersForAdditionalInfoPage(model, session);
        displayParametersForYourIncomePages(model, session);
    }
        
    private void displayParametersForPreviewPage(final Model model, final Session session) {
        model.addAttribute("displayChangeButton", displayChangeButton);
        model.addAttribute("saveButton", C3Constants.YES.equals(session.getAttribute("carerWantsContactEmail")));
        model.addAttribute("showBankDetails", previewUtils.checkDateDifference("carerDateOfBirth", "dateOfClaim", session, hidePayDetails));
        model.addAttribute("isSaveEnabled", saveForLaterSaveEnabled);
        model.addAttribute("saveForLaterUrl", saveForLaterUrl);
        model.addAttribute("beenInPreview", C3Constants.TRUE.equals(session.getAttribute("beenInPreview")));
        model.addAttribute("languageNotWelsh", !C3Constants.WELSH_LANG.equals(session.getAttribute("language")));
    }

    private void displayParametersForAboutYouPage(final Model model, final Session session) {
        model.addAttribute("carerFullName", previewUtils.getFullName("carer", session));
        model.addAttribute("maritalStatus", session.getAttribute("maritalStatus"));
        previewUtils.setDateIntoModel("carerDateOfBirth", session, model);
        model.addAttribute("carerAddressWithPostcode", previewUtils.getAddressWithPostcode("carer", session));
        model.addAttribute("carerHowWeContactYou", previewUtils.notGivenIfEmpty((String)session.getAttribute("carerHowWeContactYou"), null));
        model.addAttribute("previewCarerMail", previewUtils.mergeStrings(" - ", previewUtils.getMessage((String)session.getAttribute("carerWantsEmailContact")), (String)session.getAttribute("carerMail")));
        model.addAttribute("carerMailLabel", previewUtils.getEmailLabel(saveForLaterSaveEnabled));

        model.addAttribute("carerMaritalStatusLink", previewUtils.getLink("about_you_marital_status"));
        model.addAttribute("carerFullNameLink", previewUtils.getLink("about_you_full_name"));
        model.addAttribute("carerDateOfBirthLink", previewUtils.getLink("about_you_dob"));
        model.addAttribute("carerAddressLink", previewUtils.getLink("about_you_address"));
        model.addAttribute("carerHowWeContactYouLink", previewUtils.getLink("about_you_contact"));
        model.addAttribute("carerEmailLink", previewUtils.getLink("about_you_email"));
    }

    private void displayParametersForNationalityPage(final Model model, final Session session) {
        model.addAttribute("previewNationality", previewUtils.firstElseSecond((String)session.getAttribute("actualnationality"), (String)session.getAttribute("nationality")));
        model.addAttribute("alwaysLivedInUK", previewUtils.getMessage((String)session.getAttribute("alwaysLivedInUK")));
        model.addAttribute("liveInUKNow", previewUtils.getMessage((String)session.getAttribute("liveInUKNow")));
        model.addAttribute("arrivedInUK", previewUtils.getMessage((String)session.getAttribute("arrivedInUK")));
        previewUtils.setDateIntoModel("arrivedInUKDate", session, model);
        model.addAttribute("arrivedInUKFrom", session.getAttribute("arrivedInUKFrom"));
        model.addAttribute("trip52Weeks", previewUtils.getDetailsMessage((String)session.getAttribute("trip52Weeks")));
        model.addAttribute("eeaGuardQuestion", previewUtils.getMessage((String)session.getAttribute("eeaGuardQuestion")));
        model.addAttribute("benefitsFromEEADetails", previewUtils.getDetailsMessage((String)session.getAttribute("benefitsFromEEADetails")));
        model.addAttribute("workingForEEADetails", previewUtils.getDetailsMessage((String)session.getAttribute("workingForEEADetails")));
        model.addAttribute("showEEA", previewUtils.checkYes((String)session.getAttribute("eeaGuardQuestion")));
        model.addAttribute("showArrivedInUK", StringUtils.isNotEmpty((String)session.getAttribute("arrivedInUK")));
        model.addAttribute("showLiveInUKNow", previewUtils.checkYes((String)session.getAttribute("liveInUKNow")));

        model.addAttribute("nationalityLink", previewUtils.getLink("about_you_nationality"));
        model.addAttribute("alwaysLivedInUKLink", previewUtils.getLink("about_you_alwaysliveinuk"));
        model.addAttribute("liveInUKNowLink", previewUtils.getLink("about_you_liveinuknow"));
        model.addAttribute("arrivedInUKLink", previewUtils.getLink("about_you_arrivedinuk"));
        model.addAttribute("arrivedInUKDateLink", previewUtils.getLink("about_you_arrivedinukdate"));
        model.addAttribute("arrivedInUKFromLink", previewUtils.getLink("about_you_arrivedinukfrom"));
        model.addAttribute("trip52weeksLink", previewUtils.getLink("about_you_trip52weeks"));
        model.addAttribute("eeaGuardQuestionLink", previewUtils.getLink("about_you_eeaGuardQuestion"));
        model.addAttribute("benefitsFromEEADetailsLink", previewUtils.getLink("about_you_benefitsFromEEA"));
        model.addAttribute("workingForEEADetailsLink", previewUtils.getLink("about_you_workingForEEA"));
    }

    private void displayParametersForPartnerDetailsPage(final Model model, final Session session) {
        model.addAttribute("partnerDetailsFullName", previewUtils.getFullName("partnerDetails", session));
        model.addAttribute("hadPartnerSinceClaimDate", previewUtils.getMessage((String)session.getAttribute("hadPartnerSinceClaimDate")));
        previewUtils.setDateIntoModel("partnerDetailsDateOfBirth", session, model);
        model.addAttribute("partnerDetailsNationality", session.getAttribute("partnerDetailsNationality"));
        model.addAttribute("partnerDetailsSeparated", previewUtils.getMessage((String)session.getAttribute("partnerDetailsSeparated")));
        model.addAttribute("isPartnerPersonYouCareFor", previewUtils.getMessage((String)session.getAttribute("isPartnerPersonYouCareFor")));
        model.addAttribute("showHadPartnerSinceClaimDate", previewUtils.checkYes((String)session.getAttribute("hadPartnerSinceClaimDate")));

        model.addAttribute("hadPartnerSinceClaimDateLink", previewUtils.getLink("partner_hadPartner"));
        model.addAttribute("partnerDetailsFullNameLink", previewUtils.getLink("partner_name"));
        model.addAttribute("partnerDetailsDateOfBirthLink", previewUtils.getLink("partner_dateOfBirth"));
        model.addAttribute("partnerDetailsNationalityLink", previewUtils.getLink("partner_nationality"));
        model.addAttribute("partnerDetailsSeparatedLink", previewUtils.getLink("partner_seperated"));
        model.addAttribute("isPartnerPersonYouCareForLink", previewUtils.getLink("partner_isPersonCareFor"));
    }

    private void displayParametersForCareYouProvidePage(final Model model, final Session session) {
        model.addAttribute("careeFirstName", session.getAttribute("careeFirstName"));
        model.addAttribute("careeSurname", session.getAttribute("careeSurname"));
        model.addAttribute("careeFullName", previewUtils.getFullName("caree", session));
        previewUtils.setDateIntoModel("careeDateOfBirth", session, model);
        model.addAttribute("showHidePartnerDetails", previewUtils.checkNo((String)session.getAttribute("hadPartnerSinceClaimDate")) || previewUtils.checkNo((String)session.getAttribute("isPartnerPersonYouCareFor")));
        model.addAttribute("careeRelationship", session.getAttribute("careeRelationship"));
        model.addAttribute("careeSameAddressWithPostcode", previewUtils.getAddressIfNo("careeSameAddress", "caree", session));
        model.addAttribute("showOtherCarer", previewUtils.checkYes((String)session.getAttribute("otherCarer")));
        model.addAttribute("showOtherCarerUc", previewUtils.checkYes((String)session.getAttribute("otherCarerUc")));
        model.addAttribute("spent35HoursCaring", previewUtils.getMessage((String)session.getAttribute("spent35HoursCaring")));
        model.addAttribute("otherCarer", previewUtils.getMessage((String)session.getAttribute("otherCarer")));
        model.addAttribute("otherCarerUc", previewUtils.getMessage((String)session.getAttribute("otherCarerUc")));
        model.addAttribute("otherCarerUcDetails", previewUtils.getOnlyDetailsMessage((String)session.getAttribute("otherCarerUcDetails")));

        model.addAttribute("careeFullNameLink", previewUtils.getLink("care_you_provide_name"));
        model.addAttribute("careeDateOfBirthLink", previewUtils.getLink("care_you_provide_dob"));
        model.addAttribute("careeRelationshipLink", previewUtils.getLink("care_you_provide_relationship"));
        model.addAttribute("careeAddressLink", previewUtils.getLink("care_you_provide_address"));
        model.addAttribute("spent35HoursCaringLink", previewUtils.getLink("care_you_provide_spent35HoursCaring"));
        model.addAttribute("otherCarerLink", previewUtils.getLink("care_you_provide_otherCarer"));
        model.addAttribute("otherCarerUcLink", previewUtils.getLink("care_you_provide_otherCarerUc"));
        model.addAttribute("otherCarerUcDetailsLink", previewUtils.getLink("care_you_provide_otherCarerUcDetails"));
    }

    private void displayParametersForBreaksInCarePage(final Model model, final Session session) {
        model.addAttribute("hasBreaksForTypeHospital", StringUtils.isEmpty((String)session.getAttribute("hospitalBreakWhoInHospital")));
        model.addAttribute("hasBreaksForTypeCareHome", StringUtils.isEmpty((String)session.getAttribute("respiteBreakWhoInRespite")));
        model.addAttribute("anyBreakMessage", previewUtils.anyBreakTypeGiven((String)session.getAttribute("hospitalBreakWhoInHospital"), (String)session.getAttribute("respiteBreakWhoInRespite")));
        model.addAttribute("hospitalBreakMessage", previewUtils.breakTypeGiven((String)session.getAttribute("hospitalBreakWhoInHospital")));
        model.addAttribute("careHomeBreakMessage", previewUtils.breakTypeGiven((String)session.getAttribute("respiteBreakWhoInRespite")));
        model.addAttribute("otherBreakMessage", previewUtils.breakTypeGiven((String)session.getAttribute("carerSomewhereElseWhereYou")));

        model.addAttribute("anyBreakLink", previewUtils.getLink("breaks_breaktype"));
        model.addAttribute("hospitalBreakLink", previewUtils.getLink("breaks_hospital"));
        model.addAttribute("careHomeBreakLink", previewUtils.getLink("breaks_carehome"));
        model.addAttribute("otherBreakLink", previewUtils.getLink("breaks_breaktype_other"));
    }

    private void displayParametersForEductionPage(final Model model, final Session session) {
        model.addAttribute("showBeenInEducationSinceClaimDate", previewUtils.checkYes((String)session.getAttribute("beenInEducationSinceClaimDate")));
        model.addAttribute("beenInEducationSinceClaimDate", previewUtils.getMessage((String)session.getAttribute("beenInEducationSinceClaimDate")));
        previewUtils.setDateIntoModel("educationStartDate", session, model);
        previewUtils.setDateIntoModel("educationExpectedEndDate", session, model);
        model.addAttribute("courseTitle", session.getAttribute("courseTitle"));
        model.addAttribute("nameOfSchoolCollegeOrUniversity", session.getAttribute("nameOfSchoolCollegeOrUniversity"));
        model.addAttribute("nameOfMainTeacherOrTutor", session.getAttribute("nameOfMainTeacherOrTutor"));
        model.addAttribute("courseContactNumber", previewUtils.getDetailsNotProvidedMessage((String)session.getAttribute("courseContactNumber")));

        model.addAttribute("beenInEducationSinceClaimDateLink", previewUtils.getLink("education_beenInEducationSinceClaimDate"));
        model.addAttribute("educationStartDateLink", previewUtils.getLink("education_courseTitle"));
        model.addAttribute("courseTitleLink", previewUtils.getLink("education_nameOfSchool"));
        model.addAttribute("nameOfSchoolCollegeOrUniversityLink", previewUtils.getLink("education_nameOfTutor"));
        model.addAttribute("nameOfMainTeacherOrTutorLink", previewUtils.getLink("education_contactNumber"));
        model.addAttribute("courseContactNumberLink", previewUtils.getLink("education_startEndDates"));
    }

    private void displayParametersForBankDetailsPage(final Model model, final Session session) {
        model.addAttribute("bankDetailsEntered", previewUtils.notGivenIfEmptyElseDetailsProvided((String)session.getAttribute("accountNumber"), "preview.bankDetailsNotGiven"));
        model.addAttribute("bankDetailsLink", previewUtils.getLink("bank_details"));
    }

    private void displayParametersForAdditionalInfoPage(final Model model, final Session session) {
        model.addAttribute("additionalInfoAnswer", previewUtils.getDetailsMessage((String)session.getAttribute("anythingElse")));
        model.addAttribute("additionalInfoWelsh", previewUtils.getMessage((String)session.getAttribute("welshCommunication")));
        model.addAttribute("thirdPartyDetailsMessage", previewUtils.addThirdPartyMessage(session));

        model.addAttribute("additionalInfoAnswerLink", previewUtils.getLink("additional_info"));
        model.addAttribute("additionalInfoWelshLink", previewUtils.getLink("additional_info_welsh"));
        model.addAttribute("thirdPartyDetailsMessageLink", previewUtils.getLink("third_party"));
    }

    private void displayParametersForYourIncomePages(final Model model, final Session session) {
        model.addAttribute("isSelfEmployed", previewUtils.checkYes((String)session.getAttribute("beenSelfEmployedSince1WeekBeforeClaim")));
        model.addAttribute("isEmployed", previewUtils.checkYes((String)session.getAttribute("beenEmployedSince6MonthsBeforeClaim")));
        model.addAttribute("previewEmpAdditionalInfo", previewUtils.getDetailsMessage((String)session.getAttribute("empAdditionalInfo")));
        model.addAttribute("beenEmployedSince6MonthsBeforeClaim", previewUtils.getMessage((String)session.getAttribute("beenEmployedSince6MonthsBeforeClaim")));

        model.addAttribute("empAdditionalInfoLink", previewUtils.getLink("employment_additional_info"));
        model.addAttribute("beenEmployedSince6MonthsBeforeClaimLink", previewUtils.getLink("employment_been_employed_since"));
    }
}
