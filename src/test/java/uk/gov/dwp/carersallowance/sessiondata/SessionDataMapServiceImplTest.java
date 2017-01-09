package uk.gov.dwp.carersallowance.sessiondata;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.carersallowance.session.NoSessionException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by peterwhitehead on 30/12/2016.
 */
public class SessionDataMapServiceImplTest {
    private SessionDataMapServiceImpl sessionDataMapServiceImpl;

    @Before
    public void setUp() throws Exception {
        sessionDataMapServiceImpl = new SessionDataMapServiceImpl();
    }

    @Test
    public void testCreateAndGetSessionData() throws Exception {
        sessionDataMapServiceImpl.createSessionData("1234", "claim");
        Session session = sessionDataMapServiceImpl.getSessionData("1234");
        assertThat(session, is(notNullValue()));
        assertThat(session.getSessionId(), is("1234"));
    }

    @Test
    public void testSaveSessionData() throws Exception {
        Session session = new Session("1234");
        session = sessionDataMapServiceImpl.saveSessionData(session);
        org.assertj.core.api.Assertions.assertThat(sessionDataMapServiceImpl.getSessionData("1234")).isEqualToComparingFieldByField(session);
    }

    @Test(expected = NoSessionException.class)
    public void testRemoveSessionData() throws Exception {
        Session session = sessionDataMapServiceImpl.createSessionData("1234", "claim");
        org.assertj.core.api.Assertions.assertThat(sessionDataMapServiceImpl.getSessionData("1234")).isEqualToComparingFieldByField(session);
        sessionDataMapServiceImpl.removeSessionData("1234");
        sessionDataMapServiceImpl.getSessionData("1234");
    }
}