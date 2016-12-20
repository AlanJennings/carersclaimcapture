package uk.gov.dwp.carersallowance.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.session.SessionManager;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.application.properties")
@SpringBootTest(classes = { C3Application.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationCookieTest {
    @Inject
    private DefaultFormController formController;

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
        System.out.println("BEFORE setUp appversion:"+applicationVersion);
    }

    @Test
    public void testCookieSet() {
        try {
            when(request.getMethod()).thenReturn("GET");
            formController.handleRequest(request, response, model);
        } catch (Exception e) {

        }
        System.out.println("Response status:" + response.getStatus());
        verify(response).addCookie(cookieCaptor.capture());
        assertEquals("C3Version", cookieCaptor.getValue().getName());
        assertEquals("4.02", cookieCaptor.getValue().getValue());
    }
}