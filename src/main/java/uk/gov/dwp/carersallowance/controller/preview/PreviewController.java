package uk.gov.dwp.carersallowance.controller.preview;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
                             @Value("${save.for.later.url}") final String saveForLaterUrl) {
        super(sessionManager, messageSource, transformationManager);
        this.displayChangeButton = displayChangeButton;
        this.hidePayDetails = hidePayDetails;
        this.saveForLaterSaveEnabled = saveForLaterSaveEnabled;
        this.saveForLaterUrl = saveForLaterUrl;
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
//        @yourDetails = @{claim.questionGroup[YourDetails].getOrElse(YourDetails())}
//        @nationalityAndResidency = @{
//            claim.questionGroup[NationalityAndResidency].getOrElse(NationalityAndResidency(""))
//        }
//        @nationality = @{
//            nationalityAndResidency.actualnationality match {
//                case Some(n) => n
//                case _ => nationalityAndResidency.nationality
//            }
//        }
//        @alwayslivedinuk = @{
//            nationalityAndResidency.alwaysLivedInUK match {
//                case `yes` => messages("label.yes")
//                case _ => messages("label.no")
//            }
//        }
//        @showliveinuknow = @{
//            nationalityAndResidency.liveInUKNow match {
//                case Some(_) => true
//                case _ => false
//            }
//        }
//        @liveinuknow = @{
//            nationalityAndResidency.liveInUKNow match {
//                case Some(`yes`) => messages("label.yes")
//                case _ => messages("label.no")
//            }
//        }
//        @showarrivedinuk = @{
//            nationalityAndResidency.arrivedInUK match {
//                case Some(_) => true
//                case _ => false
//            }
//        }
//        @arrivedinuk = @{
//            nationalityAndResidency.arrivedInUK match {
//                case Some(NationalityAndResidency.lessThan3Years) => messages("arrivedInUK.less")
//                case _ => messages("arrivedInUK.more")
//            }
//        }
//        @showarrivedinukdate = @{
//            nationalityAndResidency.arrivedInUKDate match {
//                case Some(_) => true
//                case _ => false
//            }
//        }
//        @arrivedinukdate = @{
//            nationalityAndResidency.arrivedInUKDate match {
//                case Some(d) => d.`dd month, yyyy`
//                case _ => ""
//            }
//        }
//        @abroadValue = @{
//            nationalityAndResidency.trip52weeks match {
//                case `yes` => messages("label.yes") + " - " + messages("detailsProvided.simple")
//                case _ => messages("label.no")
//            }
//        }
//        @otherEEA = @{claim.questionGroup[PaymentsFromAbroad].getOrElse(PaymentsFromAbroad())}
//        @showEEA = @{
//            otherEEA.guardQuestion.answer == `yes`
//        }
//        @eeaGuardQuestionValue = @{
//            otherEEA.guardQuestion.answer match {
//                case `yes` => messages("label.yes")
//                case _ => messages("label.no")
//            }
//        }
//        @benefitsFromEEAValue = @{
//            otherEEA.guardQuestion.field1 match {
//                case Some(YesNoWith1MandatoryFieldOnYes(`yes`, _)) => messages("label.yes") + " - " + messages("detailsProvided.simple")
//                case _ => messages("label.no")
//            }
//        }
//        @workingForEEAValue = @{
//            otherEEA.guardQuestion.field2 match {
//                case Some(YesNoWith1MandatoryFieldOnYes(`yes`, _)) => messages("label.yes") + " - " + messages("detailsProvided.simple")
//                case _ => messages("label.no")
//            }
//        }
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
        model.addAttribute("showEEA", C3Constants.YES.equals(session.getAttribute("eeaGuardQuestion")));
        model.addAttribute("showArrivedInUK", checkTrue((String)session.getAttribute("arrivedInUK")));
        model.addAttribute("showLiveInUKNow", checkTrue((String)session.getAttribute("liveInUKNow")));

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

    private Boolean checkTrue(String checkValue) {
        return C3Constants.TRUE.equals(checkValue);
    }

    private String getDetailsMessage(String value) {
        if (C3Constants.YES.equals(value)) {
            return mergeStrings(" - ", getMessage(value), getMessage("preview.detailsProvided.simple"));
        }
        return getMessage(value);
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
