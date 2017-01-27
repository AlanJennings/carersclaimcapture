package uk.gov.dwp.carersallowance.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;

public class MessageSourceTestUtils {

    public static MessageSource loadMessageSource(String resourceName) throws IOException {
        if(resourceName == null) {
            return null;
        }

        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);

            Properties properties = new Properties();
            properties.load(inputStream);

            StaticMessageSource messageSource = new StaticMessageSource();
            for(String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                messageSource.addMessage(key, Locale.getDefault(), value);
            }

            return messageSource;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}