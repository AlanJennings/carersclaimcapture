package uk.gov.dwp.carersallowance.controller.started;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
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

import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.C3Constants;

@Controller
public class PreviewController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PreviewController.class);

    private static final String PAGE_NAME     = "page.preview";
    private static final String CURRENT_PAGE  = "/preview";
    private static final String REDIRECT_PAGE  = "/preview/redirect/{newPage}";
    private final Boolean displayChangeButton;
    private final Integer hidePayDetails;
    private final Boolean saveForLaterSaveEnabled;
    private final String saveForLaterUrl;

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
    public String getForm(HttpServletRequest request, Model model) {
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        displayParametersForPreviewPage(model, session);
        displayParametersForAboutYouPage(model, session);
        return super.getForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, Model model) {
        return super.postForm(request, model);
    }

    @RequestMapping(value=REDIRECT_PAGE, method = RequestMethod.GET)
    public String redirect(@PathVariable String redirectTo, @RequestParam("hash") String hash, @RequestParam("returnToSummary") String returnToSummary, HttpServletRequest request, Model model) {
        return CURRENT_PAGE;
    }

    /**
     * Might use BindingResult, and spring Validator, not sure yet
     * don't want to perform type conversion prior to controller as we have no control
     * over the (rather poor) reporting behaviour
     * @return
     */
    protected void validate(String[] fields, Map<String, String[]> fieldValues, Map<String, String[]> allFieldValues) {
        LOG.trace("Starting BenefitsController.validate");

        LOG.trace("Ending BenefitsController.validate");
    }

    public void addReturnToCheckYourAnswerLinkToSession() {

    }

    public void addHashToModel() {

    }

    public void displayParametersForPreviewPage(final Model model, final Session session) {
        model.addAttribute("displayChangeButton", displayChangeButton);
        model.addAttribute("saveButton", C3Constants.YES.equals(session.getAttribute("carerWantsContactEmail")));
        model.addAttribute("showBankDetails", checkDateDifference("carerDateOfBirth", "dateOfClaim", session, hidePayDetails));
        model.addAttribute("isSaveEnabled", saveForLaterSaveEnabled);
        model.addAttribute("saveForLaterUrl", saveForLaterUrl);
        // beenInPreview
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
        model.addAttribute("carerHowWeContactYou", session.getAttribute("carerHowWeContactYou"));
        model.addAttribute("previewCarerMail", mergeStrings(" - ", StringUtils.capitalize((String)session.getAttribute("carerWantsEmailContact")), (String)session.getAttribute("carerMail")));
        model.addAttribute("carerMailLabel", getEmailLabel());

        model.addAttribute("dateOfClaimLink", getLink("hashName", "returnToCheckYourAnswersHash", session));
        model.addAttribute("carerMaritalStatusLink", getLink("hashName", "returnToCheckYourAnswersHash", session));
        model.addAttribute("carerDateOfBirthLink", getLink("hashName", "returnToCheckYourAnswersHash", session));
        model.addAttribute("carerAddressLink", getLink("hashName", "returnToCheckYourAnswersHash", session));
        model.addAttribute("carerHowWeContactYouLink", getLink("hashName", "returnToCheckYourAnswersHash", session));
        model.addAttribute("carerEmailLink", getLink("hashName", "returnToCheckYourAnswersHash", session));
        //carerMailLabel
        //maritalStatus
        //carerFullName
        //carerAddressWithPostcode
        //carerHowWeContactYou
        //carerMail
        //dateOfClaimLink
        //carerMaritalStatusLink
        //carerDateOfBirthLink
        //carerAddressLink
        //carerHowWeContactYouLink
        //carerEmailLink

//        @yourDetails = @{claim.questionGroup[YourDetails].getOrElse(YourDetails())}
//        @contactDetails = @{claim.questionGroup[ContactDetails].getOrElse(ContactDetails())}
//        @address = @{contactDetails.address}
//        @claimDate = @{claim.questionGroup[ClaimDate].getOrElse(ClaimDate())}
//        @email = @{
//            contactDetails.wantsContactEmail match {
//                case yesNo if yesNo == yes => messages("label.yes") + " - " + contactDetails.email.getOrElse("")
//                case yesNo if yesNo == no => messages("label.no")
//                case _ => ""
//            }
//        }
//        @maritalStatusQG = @{
//            claim.questionGroup[MaritalStatus].getOrElse(MaritalStatus())
//        }
//        @maritalStatus = @{
//            maritalStatusQG.maritalStatus match {
//                case Married => messages("maritalStatus.married")
//                case Single => messages("maritalStatus.single")
//                case Divorced => messages("maritalStatus.divorced")
//                case Widowed => messages("maritalStatus.widowed")
//                case Separated => messages("maritalStatus.separated")
//                case Partner => messages("maritalStatus.livingWithPartner")
//                case _ => ""
//            }
//        }
//        @middleName = @{yourDetails.middleName.map(_+" ").getOrElse("")}
//        @titleNames = @{
//            s"${yourDetails.title} ${yourDetails.firstName} ${middleName} ${yourDetails.surname}"
//        }
//
//        @addressPostcode = @{
//            s"${address.lineOne.getOrElse("")}${address.lineTwo.fold("")("<br/>"+_)}${address.lineThree.fold("")("<br/>"+_)} ${"<br/>"+contactDetails.postcode.getOrElse("")}"
//        }
//        @saveForLaterEnabled=@{
//            getBooleanProperty("saveForLaterSaveEnabled")
//        }
//        @emailLabel=@{
//            if(saveForLaterEnabled) messages("wantsEmailContactNew")
//            else messages("wantsEmailContactOld")
//        }
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

    private String getLink(final String hashName, final String returnToCheckYourAnswersHash, final Session session) {
        return "/preview/redirect/x?hash=" + hashName + "&returnToSummary=" + returnToCheckYourAnswersHash;
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
        String fullName = mergeStrings(" ", title, firstName, middleName, surname);
        return fullName;
    }

    private String mergeStrings(final String delimiter, final String... values) {
        String merged = Arrays.asList(values).stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(delimiter));
        return merged;
    }
}
