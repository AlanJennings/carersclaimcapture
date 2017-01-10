package uk.gov.dwp.carersallowance.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by peterwhitehead on 10/01/2017.
 */
@Configuration
public class EmailConfig {
    @Bean
    public JavaMailSender mailSender(@Value("${spring.mail.host}") final String host,
                                     @Value("${spring.mail.username}") final String userName,
                                     @Value("${spring.mail.password}") final String password,
                                     @Value("${spring.mail.port}") final Integer port,
                                     @Value("${spring.mail.protocol}") final String protocol,
                                     @Value("${mailer.enabled}") final Boolean mailerEnabled) {
        if (mailerEnabled) {
            final JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(host);
            sender.setUsername(userName);
            sender.setPassword(password);
            sender.setPort(port);
            sender.setProtocol(protocol);
            return sender;
        }
        return null;
    }
}
