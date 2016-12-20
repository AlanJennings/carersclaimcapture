package uk.gov.dwp.carersallowance.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.application.properties")
@SpringBootTest(classes = { DefaultFormController.class })
public class ApplicationCookieTest {
    private DefaultFormController formController;

    @Mock
    SessionManager sessionManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Model model;

    @Value("${application.version}")
    private String applicationVersion;

    @Before
    public void setUp() throws Exception {
        System.out.println("BEFORE setUp appversion:"+applicationVersion);
        formController = new DefaultFormController(sessionManager, messageSource);
    }


    @Test
    public void testCookieSet() {
        try {
            when(request.getMethod()).thenReturn("GET");
            formController.handleRequest(request, response, model);
        } catch (Exception e) {

        }
        System.out.println("Response status:" + response.getStatus());
        System.out.println("Response:" + response.getHeader("Set-Cookie"));
        assertEquals(response.getHeader("Set-Cookie"), "1.01");
    }
}