package uk.gov.dwp.carersallowance.email;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import uk.gov.dwp.carersallowance.database.TransactionIdService;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.C3Constants;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Created by peterwhitehead on 06/01/2017.
 */
@Component
public class EmailServiceImpl implements EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final MessageSource messageSource;
    private final Boolean mailerEnabled;
    private final String mailerFrom;
    private final TransactionIdService transactionIdService;
    private final JavaMailSender javaMailSender;

    @Inject
    public EmailServiceImpl(final MessageSource messageSource,
                            @Value("${mailer.enabled}") final Boolean mailerEnabled,
                            @Value("${mailer.from}") final String mailerFrom,
                            final TransactionIdService transactionIdService,
                            final JavaMailSender javaMailSender) {
        this.messageSource = messageSource;
        this.mailerEnabled = mailerEnabled;
        this.mailerFrom = mailerFrom;
        this.transactionIdService = transactionIdService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(final String emailBody, final Session session, final String transactionId) throws Exception {
        LOG.debug("Sending email");
        try {
            if (mailerEnabled && isEmailRequired(session, transactionId)) {
                LOG.debug("Mailer enabled true, proceeding to send email.");
                if (isClaim(session)) {
                    sendClaimEmail(session, transactionId, emailBody);
                } else {
                    sendCircsEmail(session, transactionId, emailBody);
                }
                transactionIdService.updateEmailStatus(transactionId, 1);
            }
        } finally {
            LOG.debug("Finished sending email");
        }
    }

    private void sendCircsEmail(final Session session, final String transactionId, final String emailBody) throws Exception {
        final Boolean isWelshCommunication = C3Constants.YES.equals(session.getAttribute("welshCommunication"));
        final String subject = messageSource.getMessage("subject.cofc", null, null, Locale.getDefault());
        send(transactionId, subject, getEmailAddress(session), emailBody);
    }

    private void sendClaimEmail(final Session session, final String transactionId, final String emailBody) throws Exception {
        final Boolean isWelshCommunication = C3Constants.YES.equals(session.getAttribute("welshCommunication"));
        final String subject = claimEmailSubject(session, isWelshCommunication);
        send(transactionId, subject, getEmailAddress(session), emailBody);
    }

    private void send(final String transactionID, final String subject, final String emailAddress, final String body) throws Exception {
        LOG.info("Sending email for transactionId:{}", transactionID);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setText(body, "utf-8", "html");
        mimeMessage.setSubject(subject);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        mimeMessage.setFrom(mailerFrom);
        javaMailSender.send(mimeMessage);
    }

    private String claimEmailSubject(final Session session, final Boolean isWelshCommuniction) {
        if (isClaimEmployment(session) || hasSelfEmploymentPensionsAndExpenses(session)) {
            return messageSource.getMessage("subject.claim.employed", null, null, Locale.getDefault());
        }
        return messageSource.getMessage("subject.claim.notemployed", null, null, Locale.getDefault());
    }

    private Boolean hasSelfEmploymentPensionsAndExpenses(final Session session) {
        return C3Constants.YES.equals(session.getAttribute("selfEmployedPayPensionScheme"));
    }

    private Boolean isClaimEmployment(final Session session) {
        if (C3Constants.YES.equals(getEmployment(session)) || C3Constants.YES.equals(getSelfEmployment(session))) {
            return true;
        }
        return false;
    }

    private String getSelfEmployment(final Session session) {
        return (String)session.getAttribute("beenSelfEmployedSince1WeekBeforeClaim");
    }

    private String getEmployment(final Session session) {
        return (String)session.getAttribute("beenEmployedSince6MonthsBeforeClaim");
    }

    private Boolean isEmailRequired(final Session session, final String transactionId) {
        if (StringUtils.isEmpty(getEmailAddress(session))) {
            LOG.info("Not sending claim email because the user didn't input an email address for transactionId:{}", transactionId);
            return false;
        }
        return true;
    }

    private String getEmailAddress(final Session session) {
        return (String)session.getAttribute("carerMail");
    }

    private Boolean isClaim(final Session session) {
        return C3Constants.CLAIM.equals(session.getAttribute(C3Constants.KEY));
    }
}
