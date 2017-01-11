package uk.gov.dwp.carersallowance.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * Created by peterwhitehead on 10/01/2017.
 */
public class EmailLogger extends JavaMailSenderImpl {
    private static final Logger LOG = LoggerFactory.getLogger(EmailLogger.class);

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        try {
            LOG.info("Email message:{}", mimeMessage.getContent());
        } catch (IOException | MessagingException e) {
            LOG.error("unable to print message.", e);
        }
    }
}
