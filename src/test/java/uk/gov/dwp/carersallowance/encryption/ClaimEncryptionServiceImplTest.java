package uk.gov.dwp.carersallowance.encryption;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 30/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClaimEncryptionServiceImplTest {
    private ClaimEncryptionServiceImpl claimEncryptionServiceImpl;

    @Mock
    private MessageSource messageSource;

    private Session session;

    @Before
    public void setUp() throws Exception {
        createSession();
    }

    @Test
    public void testEncryptClaimOff() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("carerSurname");
        claimEncryptionServiceImpl = new ClaimEncryptionServiceImpl(false, messageSource);
        claimEncryptionServiceImpl.encryptClaim(session);
        assertThat(session.getAttribute("carerSurname"), is("Bloggs"));

    }

    @Test
    public void testEncryptClaim() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("carerSurname");
        claimEncryptionServiceImpl = new ClaimEncryptionServiceImpl(true, messageSource);
        claimEncryptionServiceImpl.encryptClaim(session);
        assertThat(session.getAttribute("carerSurname"), is(not("Bloggs")));

    }

    @Test
    public void testDecryptClaim() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("carerSurname");
        claimEncryptionServiceImpl = new ClaimEncryptionServiceImpl(true, messageSource);
        session.setAttribute("carerSurname", "+rtBjKWNQNI5lzdJukVKLkHMG8cfjDfmuygGKOxXgD7/zMztYTxu2ocZemomhhP9");
        claimEncryptionServiceImpl.decryptClaim(session);
        assertThat(session.getAttribute("carerSurname"), is("Bloggs"));
    }

    @Test
    public void testDecryptClaimOff() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("carerSurname");
        claimEncryptionServiceImpl = new ClaimEncryptionServiceImpl(false, messageSource);
        session.setAttribute("carerSurname", "+rtBjKWNQNI5lzdJukVKLkHMG8cfjDfmuygGKOxXgD7/zMztYTxu2ocZemomhhP9");
        claimEncryptionServiceImpl.decryptClaim(session);
        assertThat(session.getAttribute("carerSurname"), is("+rtBjKWNQNI5lzdJukVKLkHMG8cfjDfmuygGKOxXgD7/zMztYTxu2ocZemomhhP9"));
    }

    private void createSession() {
        session = new Session("8a20d772-b998-486d-ba05-fd3ef75d4fd2");
        session.setAttribute("carerSurname", "Bloggs");
    }
}