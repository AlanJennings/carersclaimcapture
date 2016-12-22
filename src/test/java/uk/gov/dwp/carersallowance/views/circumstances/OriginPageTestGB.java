package uk.gov.dwp.carersallowance.views.circumstances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dwp.carersallowance.controller.started.C3Application;

import java.net.URISyntaxException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = C3Application.class)
@ContextConfiguration
@WebAppConfiguration

@TestPropertySource(properties = { "origin.tag=GB"})
public class OriginPageTestGB {

    private static final String ORIGIN_PAGE             = "/circumstances/report-changes/origin";
    private static final String CHANGE_SELECTION_PAGE   = "redirect:/circumstances/report-changes/change-selection";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @Before
    public void setup() throws URISyntaxException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }


    @Test
    public void givenOriginPageRequestedThenOriginPageNotDisplayedAndForwardReturned() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(ORIGIN_PAGE);
        requestBuilder.servletPath(ORIGIN_PAGE);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CHANGE_SELECTION_PAGE))
                .andReturn();
    }

}