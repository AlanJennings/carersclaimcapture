package uk.gov.dwp.carersallowance.transformations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by peterwhitehead on 30/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransformationManagerTest {
    private TransformationManager transformationManager;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
        transformationManager = new TransformationManager();
    }

    @Test
    public void testGetTransformedValueUpperCase() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("uppercase");
        assertThat(transformationManager.getTransformedValue("testName", "testValue", "uppercase", messageSource), is("TESTVALUE"));
    }

    @Test
    public void testGetTransformedValueLowerCase() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("lowercase");
        assertThat(transformationManager.getTransformedValue("testName", "testValue", "lowercase", messageSource), is("testvalue"));
    }

    @Test
    public void testGetTransformedValueLowerCaseStripWhitespace() throws Exception {
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class))).thenReturn("stripwhitespace");
        assertThat(transformationManager.getTransformedValue("testName", " te st Val ue ", "stripwhitespace", messageSource), is("testValue"));
    }
}