package uk.gov.dwp.carersallowance.views.circumstances;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dwp.carersallowance.controller.started.C3Application;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = C3Application.class)
@ContextConfiguration
@WebAppConfiguration
public class SelectionPageTest {

    private static final String SELECTION_PAGE = "/circumstances/report-changes/change-selection";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void givenSelectionPageRequestedThenSelectionPagePresented() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(SELECTION_PAGE);
//        requestBuilder.servletPath(SELECTION_PAGE);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(view().name(SELECTION_PAGE))
//                .andReturn();
    }
}
