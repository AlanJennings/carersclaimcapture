package uk.gov.dwp.carersallowance.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.controller.started.ClaimStartedController;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.application.properties")
@SpringBootTest(classes = { C3Application.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationCookieTest {
    @Inject
    private ClaimStartedController formController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Model model;

    @Value("${application.version}")
    private String applicationVersion;

    private ArgumentCaptor<Cookie> cookieCaptor;

    @Before
    public void setUp() throws Exception {
        cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
    }

    @Test
    public void testCookieSet() {
        try {
            when(request.getMethod()).thenReturn("GET");
            when(request.getQueryString()).thenReturn(null);
            formController.getForm(request, response, model);
        } catch (Exception e) {

        }
        verify(response, times(3)).addCookie(cookieCaptor.capture());
        assertEquals("C3Version", cookieCaptor.getAllValues().get(0).getName());
        assertEquals("4.02", cookieCaptor.getAllValues().get(0).getValue());
    }
}