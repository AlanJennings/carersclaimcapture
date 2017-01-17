package uk.gov.dwp.carersallowance.controller.preview;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.utils.LoadFile;

@Controller
public class PreviewController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PreviewController.class);

    private static final String PAGE_NAME     = "page.preview";
    private static final String CURRENT_PAGE  = "/preview";
    private static final String REDIRECT_PAGE  = "/preview/redirect/{redirectTo}";
    private final Boolean displayChangeButton;
    private final Integer hidePayDetails;
    private final Boolean saveForLaterSaveEnabled;
    private final String saveForLaterUrl;
    private final String originTag;
    private static final String PREVIEW_MAPPING_CLAIM = "preview.mappings.claim";
    private final Map<String, PreviewMapping> previewMappings;

//    private static final String[] FIELDS = {};

    @Autowired
    public PreviewController(final SessionManager sessionManager,
                             final MessageSource messageSource,
                             final TransformationManager transformationManager,
                             @Value("${preview.display.change.button}") final Boolean displayChangeButton,
                             @Value("${age.hide.paydetails}") final Integer hidePayDetails,
                             @Value("${save.for.later.save.enabled}") final Boolean saveForLaterSaveEnabled,
                             @Value("${save.for.later.url}") final String saveForLaterUrl,
                             @Value("${origin.tag}") final String originTag) {
        super(sessionManager, messageSource, transformationManager);
        this.displayChangeButton = displayChangeButton;
        this.hidePayDetails = hidePayDetails;
        this.saveForLaterSaveEnabled = saveForLaterSaveEnabled;
        this.saveForLaterUrl = saveForLaterUrl;
        this.originTag = originTag;
        this.previewMappings = loadPreviewMappings();
    }

    private Map<String, PreviewMapping> loadPreviewMappings() {
        try {
            Map<String, PreviewMapping> mappings = new HashMap<>();
            URL claimTemplateUrl = this.getClass().getClassLoader().getResource(PREVIEW_MAPPING_CLAIM);
            List<String> previewMappings = LoadFile.readLines(claimTemplateUrl);
            for (String mapping : previewMappings) {
                if (StringUtils.isNotEmpty(mapping)) {
                    String[] mappingValue = mapping.split("=");
                    mappings.put(mappingValue[0].trim(), new PreviewMapping(mappingValue[1]));
                }
            }
            return mappings;
        } catch (IOException ioe) {
            LOG.error("Failed to load preview mapping file.", ioe);
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public String getCurrentPage(HttpServletRequest request) {
        return CURRENT_PAGE;
    }

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

//    @Override
//    public String[] getFields() {
//        return FIELDS;
//    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(final HttpServletRequest request, final Model model) {
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        session.setAttribute("beenInPreview", true);
        setReturnToSummaryHash(session, model);
        displayParametersForPreviewPage(model, session);
        displayParametersForAboutYouPage(model, session);
        displayParametersForNationalityPage(model, session);
        displayParametersForPartnerDetailsPage(model, session);
        displayParametersForCareYouProvidePage(model, session);
        displayParametersForBreaksInCarePage(model, session);
        displayParametersForEductionPage(model, session);
        sessionManager.saveSession(session);
        return super.getForm(request, model);
    }

    private void setReturnToSummaryHash(final Session session, final Model model) {
        final String returnToSummaryHash = (String)session.getAttribute("returnToSummary");
        if (StringUtils.isEmpty(returnToSummaryHash)) {
            return;
        }
        model.addAttribute("hash", returnToSummaryHash);
        session.removeAttribute("returnToSummary");
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, Model model) {
        return super.postForm(request, model);
    }

    @RequestMapping(value=REDIRECT_PAGE, method = RequestMethod.GET)
    public String redirect(@PathVariable String redirectTo,
                           HttpServletRequest request,
                           Model model) {
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        session.setAttribute("returnToSummary", redirectTo);
        sessionManager.saveSession(session);
        return createRedirection(redirectTo);
    }

    private String createRedirection(final String redirectTo) {
        final PreviewMapping previewMapping = previewMappings.get(redirectTo);
        return "redirect:" + previewMapping.getUrl() + "#" + previewMapping.getHash() + "_label";
    }

    public void displayParametersForPreviewPage(final Model model, final Session session) {
        model.addAttribute("displayChangeButton", displayChangeButton);
        model.addAttribute("saveButton", C3Constants.YES.equals(session.getAttribute("carerWantsContactEmail")));
        model.addAttribute("showBankDetails", checkDateDifference("carerDateOfBirth", "dateOfClaim", session, hidePayDetails));
        model.addAttribute("isSaveEnabled", saveForLaterSaveEnabled);
        model.addAttribute("saveForLaterUrl", saveForLaterUrl);
        model.addAttribute("beenInPreview", C3Constants.TRUE.equals(session.getAttribute("beenInPreview")));
        model.addAttribute("isOriginGB", isOriginGB());
    }

    public Boolean checkDateDifference(final String before, final String after, final Session session, final Integer check) {
        LocalDate dateBefore = getDate(before, session);
        LocalDate dateAfter = getDate(after, session);
        if (dateBefore != null && dateAfter != null) {
            long years = ChronoUnit.YEARS.between(dateBefore, dateAfter);
            return years < check;
        }
        return false;
    }

    public LocalDate getDate(final String date, final Session session) {
        final String day = (String)session.getAttribute(date + "_day");
        final String month = (String)session.getAttribute(date + "_month");
        final String year = (String)session.getAttribute(date + "_year");
        if (StringUtils.isNotEmpty(day) && StringUtils.isNotEmpty(month) && StringUtils.isNotEmpty(year)) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(year + month + day, formatter);
        }
        return null;
    }

    public void displayParametersForAboutYouPage(final Model model, final Session session) {
        model.addAttribute("carerFullName", getFullName("carer", session));
        model.addAttribute("maritalStatus", session.getAttribute("maritalStatus"));
        setDateIntoModel("carerDateOfBirth", session, model);
        model.addAttribute("carerAddressWithPostcode", getAddressWithPostcode("carer", session));
        model.addAttribute("carerHowWeContactYou", notGivenIfEmpty((String)session.getAttribute("carerHowWeContactYou")));
        model.addAttribute("previewCarerMail", mergeStrings(" - ", getMessage((String)session.getAttribute("carerWantsEmailContact")), (String)session.getAttribute("carerMail")));
        model.addAttribute("carerMailLabel", getEmailLabel());

        model.addAttribute("carerMaritalStatusLink", getLink("about_you_marital_status"));
        model.addAttribute("carerFullNameLink", getLink("about_you_full_name"));
        model.addAttribute("carerDateOfBirthLink", getLink("about_you_dob"));
        model.addAttribute("carerAddressLink", getLink("about_you_address"));
        model.addAttribute("carerHowWeContactYouLink", getLink("about_you_contact"));
        model.addAttribute("carerEmailLink", getLink("about_you_email"));
    }

    public void displayParametersForNationalityPage(final Model model, final Session session) {
        model.addAttribute("previewNationality", firstElseSecond((String)session.getAttribute("actualnationality"), (String)session.getAttribute("nationality")));
        model.addAttribute("alwaysLivedInUK", getMessage((String)session.getAttribute("alwaysLivedInUK")));
        model.addAttribute("liveInUKNow", getMessage((String)session.getAttribute("liveInUKNow")));
        model.addAttribute("arrivedInUK", session.getAttribute("arrivedInUK"));
        setDateIntoModel("arrivedInUKDate", session, model);
        model.addAttribute("arrivedInUKFrom", session.getAttribute("arrivedInUKFrom"));
        model.addAttribute("trip52Weeks", getDetailsMessage((String)session.getAttribute("trip52Weeks")));
        model.addAttribute("eeaGuardQuestion", getMessage((String)session.getAttribute("eeaGuardQuestion")));
        model.addAttribute("benefitsFromEEADetails", getDetailsMessage((String)session.getAttribute("benefitsFromEEADetails")));
        model.addAttribute("workingForEEADetails", getDetailsMessage((String)session.getAttribute("workingForEEADetails")));
        model.addAttribute("showEEA", checkYes((String)session.getAttribute("eeaGuardQuestion")));
        model.addAttribute("showArrivedInUK", checkYes((String)session.getAttribute("arrivedInUK")));
        model.addAttribute("showLiveInUKNow", checkYes((String)session.getAttribute("liveInUKNow")));

        model.addAttribute("nationalityLink", getLink("about_you_nationality"));
        model.addAttribute("alwaysLivedInUKLink", getLink("about_you_alwaysliveinuk"));
        model.addAttribute("liveInUKNowLink", getLink("about_you_liveinuknow"));
        model.addAttribute("arrivedInUKLink", getLink("about_you_arrivedinuk"));
        model.addAttribute("arrivedInUKDateLink", getLink("about_you_arrivedinukdate"));
        model.addAttribute("arrivedInUKFromLink", getLink("about_you_arrivedinukfrom"));
        model.addAttribute("trip52weeksLink", getLink("about_you_trip52weeks"));
        model.addAttribute("eeaGuardQuestionLink", getLink("about_you_eeaGuardQuestion"));
        model.addAttribute("benefitsFromEEADetailsLink", getLink("about_you_benefitsFromEEA"));
        model.addAttribute("workingForEEADetailsLink", getLink("about_you_workingForEEA"));
    }

    public void displayParametersForPartnerDetailsPage(final Model model, final Session session) {
        model.addAttribute("partnerDetailsFullName", getFullName("partnerDetails", session));
        model.addAttribute("hadPartnerSinceClaimDate", getMessage((String)session.getAttribute("hadPartnerSinceClaimDate")));
        setDateIntoModel("partnerDetailsDateOfBirth", session, model);
        model.addAttribute("partnerDetailsNationality", session.getAttribute("partnerDetailsNationality"));
        model.addAttribute("partnerDetailsSeparated", getMessage((String)session.getAttribute("partnerDetailsSeparated")));
        model.addAttribute("isPartnerPersonYouCareFor", getMessage((String)session.getAttribute("isPartnerPersonYouCareFor")));
        model.addAttribute("showHadPartnerSinceClaimDate", checkYes((String)session.getAttribute("hadPartnerSinceClaimDate")));

        model.addAttribute("hadPartnerSinceClaimDateLink", getLink("partner_hadPartner"));
        model.addAttribute("partnerDetailsFullNameLink", getLink("partner_name"));
        model.addAttribute("partnerDetailsDateOfBirthLink", getLink("partner_dateOfBirth"));
        model.addAttribute("partnerDetailsNationalityLink", getLink("partner_nationality"));
        model.addAttribute("partnerDetailsSeparatedLink", getLink("partner_seperated"));
        model.addAttribute("isPartnerPersonYouCareForLink", getLink("partner_isPersonCareFor"));
    }

    public void displayParametersForCareYouProvidePage(final Model model, final Session session) {
        model.addAttribute("careeFullName", getFullName("caree", session));
        setDateIntoModel("careeDateOfBirth", session, model);
        model.addAttribute("showHidePartnerDetails", checkNo((String)session.getAttribute("hadPartnerSinceClaimDate")) || checkNo((String)session.getAttribute("isPartnerPersonYouCareFor")));
        model.addAttribute("careeRelationship", session.getAttribute("careeRelationship"));
        model.addAttribute("careeSameAddressWithPostcode", getAddressIfNo("careeSameAddress", "caree", session));

        model.addAttribute("careeFullNameLink", getLink("care_you_provide_name"));
        model.addAttribute("careeDateOfBirthLink", getLink("care_you_provide_dob"));
        model.addAttribute("careeRelationshipLink", getLink("care_you_provide_relationship"));
        model.addAttribute("careeAddressLink", getLink("care_you_provide_address"));
    }

    public void displayParametersForBreaksInCarePage(final Model model, final Session session) {
        model.addAttribute("hasBreaksForTypeHospital", StringUtils.isEmpty((String)session.getAttribute("hospitalBreakWhoInHospital")));
        model.addAttribute("hasBreaksForTypeCareHome", StringUtils.isEmpty((String)session.getAttribute("respiteBreakWhoInRespite")));
        model.addAttribute("anyBreakMessage", anyBreakTypeGiven((String)session.getAttribute("hospitalBreakWhoInHospital"), (String)session.getAttribute("respiteBreakWhoInRespite")));
        model.addAttribute("hospitalBreakMessage", breakTypeGiven((String)session.getAttribute("hospitalBreakWhoInHospital")));
        model.addAttribute("careHomeBreakMessage", breakTypeGiven((String)session.getAttribute("respiteBreakWhoInRespite")));
        model.addAttribute("otherBreakMessage", breakTypeGiven((String)session.getAttribute("carerSomewhereElseWhereYou")));

        model.addAttribute("anyBreakLink", getLink("breaks_breaktype"));
        model.addAttribute("hospitalBreakLink", getLink("breaks_hospital"));
        model.addAttribute("careHomeBreakLink", getLink("breaks_carehome"));
        model.addAttribute("otherBreakLink", getLink("breaks_breaktype_other"));
    }

    public void displayParametersForEductionPage(final Model model, final Session session) {
        model.addAttribute("showBeenInEducationSinceClaimDate", checkYes((String)session.getAttribute("beenInEducationSinceClaimDate")));
        model.addAttribute("beenInEducationSinceClaimDate", getMessage((String)session.getAttribute("beenInEducationSinceClaimDate")));
        setDateIntoModel("educationStartDate", session, model);
        setDateIntoModel("educationExpectedEndDate", session, model);
        model.addAttribute("courseTitle", session.getAttribute("courseTitle"));
        model.addAttribute("nameOfSchoolCollegeOrUniversity", session.getAttribute("nameOfSchoolCollegeOrUniversity"));
        model.addAttribute("nameOfMainTeacherOrTutor", session.getAttribute("nameOfMainTeacherOrTutor"));
        model.addAttribute("courseContactNumber", getDetailsNotProvidedMessage((String)session.getAttribute("courseContactNumber")));

        model.addAttribute("beenInEducationSinceClaimDateLink", getLink("education_beenInEducationSinceClaimDate"));
        model.addAttribute("educationStartDateLink", getLink("education_courseTitle"));
        model.addAttribute("courseTitleLink", getLink("education_nameOfSchool"));
        model.addAttribute("nameOfSchoolCollegeOrUniversityLink", getLink("education_nameOfTutor"));
        model.addAttribute("nameOfMainTeacherOrTutorLink", getLink("education_contactNumber"));
        model.addAttribute("courseContactNumberLink", getLink("education_startEndDates"));
    }

    private String breakTypeGiven(final String value) {
        if (StringUtils.isEmpty(value)) {
            getMessage(C3Constants.NO);
        }
        return getDetailsMessage(C3Constants.YES);
    }

    private String anyBreakTypeGiven(final String... value) {
        if (ArrayUtils.isEmpty(value)) {
            getMessage(C3Constants.NO);
        }
        return getMessage(C3Constants.YES);
    }

    private Boolean isOriginGB() {
        return "GB".equals(originTag);
    }

    private Boolean checkYes(String checkValue) {
        return C3Constants.YES.equals(checkValue);
    }

    private Boolean checkNo(String checkValue) {
        return C3Constants.NO.equals(checkValue);
    }

    private String getDetailsMessage(String value) {
        if (C3Constants.YES.equals(value)) {
            return mergeStrings(" - ", getMessage(value), getMessage("preview.detailsProvided.simple"));
        }
        return getMessage(value);
    }

    private String getDetailsNotProvidedMessage(String value) {
        if (StringUtils.isEmpty(value)) {
            return getMessage("preview.detailsNotProvided");
        }
        return value;
    }

    private String firstElseSecond(final String first, String second) {
        if (StringUtils.isNotEmpty(first)) {
            return first;
        }
        return second;
    }

    private void setDateIntoModel(final String date, final Session session, final Model model) {
        model.addAttribute(date + "_day", session.getAttribute(date + "_day"));
        model.addAttribute(date + "_month", session.getAttribute(date + "_month"));
        model.addAttribute(date + "_year", session.getAttribute(date + "_year"));
    }

    private String getEmailLabel() {
        if (saveForLaterSaveEnabled) {
            return "carerWantsEmailContactNew.label";
        }
        return "carerWantsEmailContact.label";
    }

    private String getLink(final String indexIntoList) {
        return "/preview/redirect/" + indexIntoList;
    }

    private String getAddressIfNo(String addCheckValue, final String startIndex, final Session session) {
        final String checkValue = (String)session.getAttribute(addCheckValue);
        if (C3Constants.NO.equals(checkValue)) {
            return getMessage(checkValue) + "<br/><br/>" + getAddressWithPostcode(startIndex, session);
        }
        return getMessage(checkValue);
    }

    private String getAddressWithPostcode(final String startIndex, final Session session) {
        final String lineOne = (String)session.getAttribute(startIndex + "AddressLineOne");
        final String lineTwo = (String)session.getAttribute(startIndex + "AddressLineTwo");
        final String lineThree = (String)session.getAttribute(startIndex + "AddressLineThree");
        final String postcode = (String)session.getAttribute(startIndex + "Postcode");
        String address = mergeStrings("<br/>", lineOne, lineTwo, lineThree, postcode);
        return address;
    }

    private String getFullName(final String startIndex, final Session session) {
        final String title = (String)session.getAttribute(startIndex + "Title");
        final String firstName = (String)session.getAttribute(startIndex + "FirstName");
        final String middleName = (String)session.getAttribute(startIndex + "MiddleName");
        final String surname = (String)session.getAttribute(startIndex + "Surname");
        final String fullName = mergeStrings(" ", title, firstName, middleName, surname);
        return fullName;
    }

    private String mergeStrings(final String delimiter, final String... values) {
        String merged = Arrays.asList(values).stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(delimiter));
        return merged;
    }

    private String notGivenIfEmpty(final String value) {
        if (StringUtils.isEmpty(value)) {
            return getMessage("preview.notgiven");
        }
        return value;
    }

    private String getMessage(final String code) {
        return messageSource.getMessage(code, null, null, Locale.getDefault());
    }
}
