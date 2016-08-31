package uk.gov.dwp.carersallowance.xml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import uk.gov.dwp.carersallowance.utils.Parameters;

/**
 * See xml.XmlHelper.scala
 * @author drh
 * encrypted details are:
 *    FullName
 *    surname
 *    nationalInsuranceNumber
 *    postcode
 *    accountHolderName (bankBuildingSocietyDetails)
 *    accountNumber (bankBuildingSocietyDetails)
 *    sortCode (bankBuildingSocietyDetails)
 *
 * Can we derive this code from a set of rules? e.g. have section tag wrap the page values etc.
 */
public class ClaimXml {

    private DocumentBuilderFactory docFactory;
    private DocumentBuilder        docBuilder;
    private List<String>           encryptionXPaths;

    public ClaimXml() throws ParserConfigurationException {
        encryptionXPaths = new ArrayList<>();       // this is largely to accumulate the list

        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
    }

    private Element addNode(Document document, Node parent, String tagName, String value, boolean ignoreIfEmpty) {
        if(ignoreIfEmpty && StringUtils.isEmpty(value)) {
            return null;
        }
        return addNode(document, parent, tagName, value);
    }

    private Element addNode(Document document, Node parent, String tagName, String value) {
        Parameters.validateMandatoryArgs(new Object[]{document, parent, tagName}, new String[]{"document", "parent", "tagName"});

        Element element = document.createElement(tagName);
        parent.appendChild(element);
        if(StringUtils.isNotEmpty(value)) {
            Text textNode = document.createTextNode(value);    // TODO should this be a CDATA element?
            parent.appendChild(textNode);
        }

        return element;
    }

    private Element addElementWithAttributes(Document document, Node parent, String tagName, String attributeName, String attributeValue) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(attributeName, attributeValue);
        return addElementWithAttributes(document, parent, tagName, attributes);
    }

    private Element addElementWithAttributes(Document document, Node parent, String tagName, Map<String, String> attributes) {
        Parameters.validateMandatoryArgs(new Object[]{document, parent, tagName}, new String[]{"document", "parent", "tagName"});
        Element node = addNode(document, parent, tagName, null);
        if(attributes != null) {
            for(Map.Entry<String, String> entry: attributes.entrySet()) {
                node.setAttribute(entry.getKey(), entry.getValue());
            }
        }

        return node;
    }

    private String getResourceMessage(String key, Object...parameters) {
        return key; // TODO implement this
    }

    private Element addQuestionNode(Document document, Node parent, String tagName, String questionLabelKey, String questionAnswer, Object...parameters) {
        Parameters.validateMandatoryArgs(questionLabelKey, "questionLabel");

        Element node = addNode(document, parent, tagName, null);
        addNode(document, node, "QuestionLabel", getResourceMessage(questionLabelKey, parameters));
        addNode(document, node, "Answer", questionAnswer);

        return node;
    }

    /**
     * TODO consider a standard collection such as MapList or something to replace Address
     * @param document
     * @param parent
     * @param tagName
     * @param questionLabelKey
     * @param address
     *
     * TODO probably use OrderedMap for this
     */
    private Element addAddressNode(Document document, Node parent, String tagName, String questionLabelKey, Object addressObj) {
        Element addressNode = addNode(document, parent, tagName, null);

        addNode(document, addressNode, "Line", "addressObj.getAddressLine1", false);
        addNode(document, addressNode, "Line", "addressObj.getAddressLine2", false);
        addNode(document, addressNode, "Line", "addressObj.getAddressLine3", false);
        addNode(document, addressNode, "PostCode", "postcode", false);

        return addressNode;
    }

    private Element addDateQuestionNode(Document document, Node parent, String tagName, String questionLabelKey, Object addressObj) {
        throw new UnsupportedOperationException("addDateQuestionNode");
    }

    public Document buildXml(Object claim, String transactionId, String schemaLocation, String version, String claimVersion, String origin, String language) {

        Document document = docBuilder.newDocument();
        Element dwpBody = document.createElement("DWPBody");
        document.appendChild(dwpBody);
        dwpBody.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        dwpBody.setAttribute("xmlns", "http://www.govtalk.gov.uk/dwp/carers-allowance");
        dwpBody.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        dwpBody.setAttribute("xsi:schemaLocation", schemaLocation);

        addNode(document, dwpBody, "Version", version);
        addNode(document, dwpBody, "ClaimVersion", claimVersion);
        addNode(document, dwpBody, "Origin", origin);

        Node dwpCaTransaction = addElementWithAttributes(document, dwpBody, "DWPCATransaction", "id", transactionId);
        addNode(document, dwpCaTransaction, "TransactionId", transactionId);
        addNode(document, dwpCaTransaction, "DateTimeGenerated", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));
        addNode(document, dwpCaTransaction, "LanguageUsed", language);

        buildDwpCaClaim(document, dwpCaTransaction, claim);
        return document;
    }

    private Node buildDwpCaClaim(Document document, Node parent, Object claim) {
        Node dwpCaClaim = addNode(document, parent, "DWPCAClaim", null);

//        Object courseDetails = claim.questionGroup[YourCourseDetails].getOrElse(YourCourseDetails(beenInEducationSinceClaimDate = no));
//        Object claimDate = claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`);
//        Object yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails());
//        Object havePartner = yourPartnerPersonalDetails.hadPartnerSinceClaimDate;
//        Object qualifyingBenefit = claim.questionGroup[Benefits].getOrElse(Benefits());

        addQuestionNode(document, dwpCaClaim, "DateOfClaim", "<label>", "<answer>");
        addQuestionNode(document, dwpCaClaim, "QualifyingBenefit", "<label>", "<answer>");
            addClaimantNode(document, dwpCaClaim, claim);
            addCareeNode(document, dwpCaClaim, claim);
            addResidencyNode(document, dwpCaClaim, claim);
        addQuestionNode(document, dwpCaClaim, "CourseOfEducation", "<label>", "<answer>", "claimDate parameter");
            addFullTimeEducationNode(document, dwpCaClaim, claim);
            addIncomesNode(document, dwpCaClaim, claim);
        addQuestionNode(document, dwpCaClaim, "HavePartner", "<label>", "<answer>", "claimDate parameter");
            addPartnerNode(document, dwpCaClaim, claim);
            addOtherBenefitsNode(document, dwpCaClaim, claim);
            addPaymentNode(document, dwpCaClaim, claim);
            addOtherInformationNode(document, dwpCaClaim, claim);
            addDeclarationNode(document, dwpCaClaim, claim);
            addDisclaimerNode(document, dwpCaClaim, claim);
            addEvidenceListNode(document, dwpCaClaim, claim);
            addConsentsNode(document, dwpCaClaim, claim);
            addAssistedDecisionNode(document, dwpCaClaim, claim);

        return dwpCaClaim;
    }

    private Node addClaimantNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Claimant", null);

        addQuestionNode(document, node, "Surname", "surname", "yourDetails.surname");       // encrypt
        addQuestionNode(document, node, "OtherNames", "firstName", "yourDetails.firstName");
        addQuestionNode(document, node, "MiddleNames", "middleName", "yourDetails.middleName");
        addQuestionNode(document, node, "Title", "title", "yourDetails.title");
        addQuestionNode(document, node, "DateOfBirth","dateOfBirth", "yourDetails.dateOfBirth");
        addQuestionNode(document, node, "NationalInsuranceNumber","nationalInsuranceNumber", "yourDetails.nationalInsuranceNumber");  //encrypt
            addAddressNode(document, node, "Address", "address", "contactDetails.address");  // postcode is encrypted (uppercase?)
        addQuestionNode(document, node, "DayTimePhoneNumber","howWeContactYou", "contactDetails.howWeContactYou");
        addQuestionNode(document, node, "Cared35HoursBefore","spent35HoursCaringBeforeClaim.label", "claimDateDetails.spent35HoursCaringBeforeClaim.answer, claim.dateOfClaim.fold({NO CLAIM DATE})(dmy => displayPlaybackDatesFormat(Lang(en),dmy))");
            addDateQuestionNode(document, node, "DayTimePhoneNumber","DateStartCaring", "contactDetails.howWeContactYou");
        addQuestionNode(document, node, "MaritalStatus", "maritalStatus", "maritalStatus.maritalStatus");
        addQuestionNode(document, node, "TextPhoneContact","contactYouByTextphone", "textPhone(contactDetails)");
        addQuestionNode(document, node, "WantsContactEmail","wantsEmailContact", "contactDetails.wantsContactEmail");
        addQuestionNode(document, node, "Email","mail.output", "contactDetails.email");

        return node;
    }

    private Node addCareeNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Caree", null);

        addQuestionNode(document, node, "Surname", "surname", "theirPersonalDetails.surname");       // encrypt
        addQuestionNode(document, node, "OtherNames", "firstName", "theirPersonalDetails.firstName");
        addQuestionNode(document, node, "MiddleNames", "middleName", "theirPersonalDetails.middleName");
        addQuestionNode(document, node, "Title", "title", "theirPersonalDetails.title");
        addQuestionNode(document, node, "DateOfBirth", "dateOfBirth", "theirPersonalDetails.dateOfBirth.`dd-MM-yyyy`");
        addQuestionNode(document, node, "NationalInsuranceNumber", "nationalInsuranceNumber", "theirPersonalDetails.nationalInsuranceNumber");  // encrypt
            addAddressNode(document, node, "Address", "address", "theirPersonalDetails.theirAddress.address");  // postcode is encrypted (uppercase?)
        addQuestionNode(document, node, "RelationToClaimant", "relationship", "theirPersonalDetails.relationship");
        addQuestionNode(document, node, "Cared35Hours", "spent35HoursCaring", "moreAboutTheCare.spent35HoursCaring, dpName");
        addQuestionNode(document, node, "OtherCarer", "otherCarer", "moreAboutTheCare.otherCarer, dpName");
        addQuestionNode(document, node, "OtherCarerUc", "otherCarerUc", "moreAboutTheCare.otherCarerUc, dpName");
        addQuestionNode(document, node, "OtherCarerUcDetails", "otherCarerUcDetails", "moreAboutTheCare.otherCarerUcDetails");
//                {careBreak(claim");
        addQuestionNode(document, node, "LiveSameAddress", "theirAddress.answer", "theirPersonalDetails.theirAddress.answer");

        return node;
    }

    private Node addResidencyNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Residency", null);

//            {question(<Nationality/>, "nationality", nationality)}
//                {nationalityAndResidency.nationality match {
//                  case NationalityAndResidency.anothercountry => question(<ActualNationality/>, "actualnationality.text.label", nationalityAndResidency.actualnationality)
//                  case _ => NodeSeq.Empty
//                }}
//
            addQuestionNode(document, node, "AlwaysLivedInUK", "alwaysLivedInUK", "nationalityAndResidency.alwaysLivedInUK");
            addQuestionNode(document, node, "LiveInUKNow", "liveInUKNow", "nationalityAndResidency.liveInUKNow");
            addQuestionNode(document, node, "ArrivedInUK", "arrivedInUK", "nationalityAndResidency.arrivedInUK");
            addQuestionNode(document, node, "ArrivedInUKDate", "arrivedInUKDate", "nationalityAndResidency.arrivedInUKDate");
            addQuestionNode(document, node, "ArrivedInUKFrom", "arrivedInUKFrom", "nationalityAndResidency.arrivedInUKFrom");
            addQuestionNode(document, node, "TimeOutsideGBLast3Years", "trip52weeks", "nationalityAndResidency.trip52weeks");
            addQuestionNode(document, node, "TripDetails", "tripDetails", "nationalityAndResidency.tripDetails");

      return node;
    }

    private Node addFullTimeEducationNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "FullTimeEducation", null);

//        <FullTimeEducation>
//            {courseDetailsXml(claim)}
//            {locationDetailsXml(claim)}
//        </FullTimeEducation>

//        uses

//        <CourseDetails>
//            {question(<Title/>,"courseTitle",courseDetails.title)}
//            {question(<DateStarted/>, "startDate", courseDetails.startDate)}
//            {question(<ExpectedEndDate/>, "expectedEndDate", courseDetails.expectedEndDate)}
//        </CourseDetails>

//        and

//        <LocationDetails>
//            {question(<Name/>,"nameOfSchoolCollegeOrUniversity",courseDetails.nameOfSchoolCollegeOrUniversity)}
//            {question(<PhoneNumber/>,"courseContactNumber", courseDetails.courseContactNumber)}
//            {question(<Tutor/>,"nameOfMainTeacherOrTutor", courseDetails.nameOfMainTeacherOrTutor)}
//        </LocationDetails>

        return node;
    }

    private Node addIncomesNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Incomes", null);

//            addQuestionNode(document, node, "Employed", "aboutYou_beenEmployedSince6MonthsBeforeClaim.label", incomes.beenEmployedSince6MonthsBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 6 months}")(dmy => DWPCAClaim.displayClaimDate(dmy - 6 months)), claimDate)}
//            addQuestionNode(document, node, "SelfEmployed", "aboutYou_beenSelfEmployedSince1WeekBeforeClaim.label", incomes.beenSelfEmployedSince1WeekBeforeClaim, claim.dateOfClaim.fold("{CLAIM DATE - 1 week}")(dmy => DWPCAClaim.displayClaimDate(dmy - 1 week)), claimDate)}
            addQuestionNode(document, node, "OtherPaymentQuestion", "yourIncome.otherIncome.label", "anyOtherPayments", "claimDate");
            addQuestionNode(document, node, "SickPayment", "yourIncome.ssp", "incomes.yourIncome_sickpay");
            addQuestionNode(document, node, "PatMatAdopPayment", "yourIncome.spmp", "incomes.yourIncome_patmatadoppay");
            addQuestionNode(document, node, "FosteringPayment", "yourIncome.fostering", "incomes.yourIncome_fostering");
            addQuestionNode(document, node, "DirectPayment", "yourIncome.direct", "incomes.yourIncome_directpay");
            addQuestionNode(document, node, "AnyOtherPayment", "yourIncome.anyother", "incomes.yourIncome_anyother");
            addQuestionNode(document, node, "NoOtherPayment", "yourIncome.none", "incomes.yourIncome_none");
//                {Employment.xml(claim)}
//                {SelfEmployment.xml(claim)}
//                {if(!empAdditionalInfo.empAdditionalInfo.answer.isEmpty) questionOther(<EmploymentAdditionalInfo/>, "empAdditionalInfo.answer", empAdditionalInfo.empAdditionalInfo.answer, empAdditionalInfo.empAdditionalInfo.text)}
//                {sickPayXml(claim)}
//                {statPatMatAdoptPayXml(claim)}
//                {fosteringAllowanceXml(claim)}
//                {directPaymentXml(claim)}
//                {otherPaymentsXml(claim)}

        return node;
    }

    private Node addPartnerNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Partner", null);

//        val yourPartnerPersonalDetails = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
//        val personYouCareFor = claim.questionGroup[YourPartnerPersonalDetails].getOrElse(YourPartnerPersonalDetails())
//        val hadPartner = (yourPartnerPersonalDetails.hadPartnerSinceClaimDate == yes)
//
//        if (hadPartner) {
//          <Partner>
//            {question(<Surname/>,"surname",encrypt(yourPartnerPersonalDetails.surname))}
//            {question(<OtherNames/>, "firstName", yourPartnerPersonalDetails.firstName.getOrElse(""))}
//            {question(<MiddleNames/>, "middleName", yourPartnerPersonalDetails.middleName)}
//            {question(<OtherSurnames/>,"otherNames", yourPartnerPersonalDetails.otherSurnames)}
//            {question(<Title/>, "title", yourPartnerPersonalDetails.title)}
//            {question(<DateOfBirth/>,"dateOfBirth", yourPartnerPersonalDetails.dateOfBirth)}
//            {question(<NationalInsuranceNumber/>,"nationalInsuranceNumber",encrypt(yourPartnerPersonalDetails.nationalInsuranceNumber))}
//            {question(<NationalityPartner/>, "partner.nationality", yourPartnerPersonalDetails.nationality)}
//            <RelationshipStatus>
//              {question(<SeparatedFromPartner/>, "separated_fromPartner.label", yourPartnerPersonalDetails.separatedFromPartner, claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => displayPlaybackDatesFormat(Lang("en"), dmy)))}
//            </RelationshipStatus>
//            {question(<IsCaree/>, "isPartnerPersonYouCareFor", personYouCareFor.isPartnerPersonYouCareFor)}
//          </Partner>
//        }
        return node;
    }

    private Node addOtherBenefitsNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "OtherBenefits", null);

//        val otherEEAState = claim.questionGroup[PaymentsFromAbroad].getOrElse(PaymentsFromAbroad())
//        <OtherBenefits>
//          <EEA>
//            {question(<EEAGuardQuestion/>,"eeaGuardQuestion.answer", otherEEAState.guardQuestion.answer)}
//            {question(<EEAReceivePensionsBenefits/>,"benefitsFromEEA", otherEEAState.guardQuestion.field1.fold("")(_.answer))}
//            {question(<EEAReceivePensionsBenefitsDetails/>,"benefitsFromEEADetails", otherEEAState.guardQuestion.field1.fold(Option[String](""))(_.field))}
//            {question(<EEAWorkingInsurance/>,"workingForEEA", otherEEAState.guardQuestion.field2.fold("")(_.answer))}
//            {question(<EEAWorkingInsuranceDetails/>,"workingForEEADetails", otherEEAState.guardQuestion.field2.fold(Option[String](""))(_.field))}
//          </EEA>
//        </OtherBenefits>

          return node;
    }

    private Node addPaymentNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Payment", null);

//        val howWePayYou: HowWePayYou = claim.questionGroup[HowWePayYou].getOrElse(HowWePayYou())
//        val showAccount = howWePayYou.likeToBePaid == "yes"

//        claim.questionGroup[HowWePayYou] match {
//          case Some(how) => {

//              {question(<PaymentFrequency/>,"paymentFrequency", how.paymentFrequency)}
//              {question(<InitialAccountQuestion/>,"likeToPay", how.likeToBePaid)}
//              {if (showAccount) account(howWePayYou) else NodeSeq.Empty}

//          }


        return node;
    }

    private Node addOtherInformationNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "OtherInformation", null);

//        val additionalInfo = claim.questionGroup[models.domain.AdditionalInfo].getOrElse(models.domain.AdditionalInfo())
//        <OtherInformation>
//          {question(<WelshCommunication/>,"welshCommunication",additionalInfo.welshCommunication)}
//          {questionWhy(<AdditionalInformation/>,"anythingElse.answer", additionalInfo.anythingElse.answer, additionalInfo.anythingElse.text,"anythingElse.text")}
//        </OtherInformation>

                return node;
    }

    private Node addDeclarationNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Declaration", null);

//        val thirdParty = claim.questionGroup[models.domain.ThirdPartyDetails].getOrElse(models.domain.ThirdPartyDetails())
//
//        <Declaration>
//          <DeclarationStatement>
//            <Content>{messagesApi("declaration.openingParagraph")}</Content>
//            <Content>{messagesApi("declaration.correct")}</Content>
//            {if(isOriginGB){<Content>{messagesApi("declaration.maycheck")}</Content>}}
//            <Content>{messagesApi("declaration.overpayment")}</Content>
//            <Content>{messagesApi("declaration.reportChanges.pdf")}</Content>
//            <Content>{messagesApi("declaration.warning")}</Content>
//          </DeclarationStatement>
//          {question(<DeclarationQuestion/>,"thirdParty", yesNoText(thirdParty))}
//          {question(<DeclarationQuestion/>,"agreement", "Yes")}
//          {if(thirdParty.thirdParty == ThirdPartyDetails.noCarer){
//            {question(<DeclarationNameOrg/>,"thirdParty.nameAndOrganisation", thirdParty.nameAndOrganisation.getOrElse(""))}
//          }}
//        </Declaration>

        return node;
    }

    private Node addDisclaimerNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Disclaimer", null);

//        val disclaimer = claim.questionGroup[models.domain.Disclaimer].getOrElse(models.domain.Disclaimer())
//
//        <Disclaimer>
//          <DisclaimerStatement>
//            <Title>This is my claim for Carer's Allowance.</Title>
//            <Content>{messagesApi("disclaimer.1")}</Content>
//            <Content>{messagesApi("disclaimer.2")}</Content>
//            <Content>{messagesApi("disclaimer.3")}</Content>
//            {if(isOriginGB){<Content>{messagesApi("disclaimer.4", "", "")}</Content>}}
//            <Content>{messagesApi("disclaimer.5", "", "")}</Content>
//            <Content>{messagesApi("disclaimer.6", "", "")}</Content>
//          </DisclaimerStatement>
//          {question(<DisclaimerQuestion/>,"read", disclaimer.read)}
//        </Disclaimer>

        return node;
    }

    private Node addEvidenceListNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "EvidenceList", null);

        return node;
    }

    private Node addConsentsNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "Consents", null);

        return node;
    }

    private Node addAssistedDecisionNode(Document document, Node dwpCaClaim, Object claim) {
        Element node = addNode(document, dwpCaClaim, "AssistedDecisions", null);

        return node;
    }

}
