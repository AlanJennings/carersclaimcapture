package uk.gov.dwp.carersallowance.session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.dwp.carersallowance.utils.C3Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CookieManagerTest {
    private CookieManager cookieManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ArgumentCaptor<Cookie> cookieCaptor;

    @Before
    public void setUp() throws Exception {
        cookieManager = new CookieManager(90, "_ga", C3Constants.CLAIM, 90, "GA1.1", 90, "4.02-SNAPSHOT", "C3Version");
        cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
    }

    @Test
    public void testAddVersionCookie() throws Exception {
        cookieManager.addVersionCookie(response);
        verify(response, times(1)).addCookie(cookieCaptor.capture());
        assertThat("C3Version", is(cookieCaptor.getAllValues().get(0).getName()));
        assertThat("4.02", is(cookieCaptor.getAllValues().get(0).getValue()));
    }

    @Test
    public void testAddGaCookie() throws Exception {
        cookieManager.addGaCookie(request, response);
        verify(response, times(1)).addCookie(cookieCaptor.capture());
        assertThat("_ga", is(cookieCaptor.getAllValues().get(0).getName()));
    }

    @Test
    public void testAddSessionCookie() throws Exception {
        cookieManager.addSessionCookie(response, "1234");
        verify(response, times(1)).addCookie(cookieCaptor.capture());
        assertThat(C3Constants.CLAIM, is(cookieCaptor.getAllValues().get(0).getName()));
        assertThat("1234", is(cookieCaptor.getAllValues().get(0).getValue()));
    }
}