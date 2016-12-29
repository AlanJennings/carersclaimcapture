package uk.gov.dwp.carersallowance.views.circumstances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = C3Application.class)
@ContextConfiguration
@WebAppConfiguration

@TestPropertySource(properties = { "origin.tag=GB-NIR", "session.data.to.db=false"})
public class OriginPageTestNI {

    private static final String ORIGIN_PAGE = "/circumstances/report-changes/selection";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @Before
    public void setup() throws URISyntaxException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }


    @Test
    public void givenOriginPageRequestedThenOriginPagePresented() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(ORIGIN_PAGE);
        requestBuilder.servletPath(ORIGIN_PAGE);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name(ORIGIN_PAGE))
                .andReturn();
    }

}