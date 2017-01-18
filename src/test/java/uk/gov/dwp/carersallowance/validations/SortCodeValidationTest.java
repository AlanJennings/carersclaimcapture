package uk.gov.dwp.carersallowance.validations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.dwp.carersallowance.controller.started.C3Application;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = C3Application.class)
public class SortCodeValidationTest {

    private SortCodeValidation sortCodeValidation;
    private ValidationSummary validationSummary;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TransformationManager transformationManager;

    private final String FIELD_NAME = "sortCode";
    private Map<String, String[]> requestFieldValues;
    private Map<String, String[]> existingFieldValues;

    @Before
    public void setup() {
        sortCodeValidation = new SortCodeValidation("true");
        validationSummary = new ValidationSummary();
        requestFieldValues = new HashMap<>();
        existingFieldValues = new HashMap<>();
        initMocks(this);
    }

    @Test
    public void givenSortCodeThatIsTooShortThenShouldReturnFalse() {
        requestFieldValues.put(FIELD_NAME + "_1", new String[]{"1"});
        requestFieldValues.put(FIELD_NAME + "_2", new String[]{"22"});
        requestFieldValues.put(FIELD_NAME + "_3", new String[]{"33"});

        boolean validationResult = sortCodeValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(false)));
    }

    @Test
    public void givenEmptySortCodeFieldThenShouldReturnFalse() {
        requestFieldValues.put(FIELD_NAME + "_1", new String[]{""});
        requestFieldValues.put(FIELD_NAME + "_2", new String[]{""});
        requestFieldValues.put(FIELD_NAME + "_3", new String[]{""});

        boolean validationResult = sortCodeValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(false)));
    }

    @Test
    public void givenNonNumericInputThenShouldReturnFalse() {
        requestFieldValues.put(FIELD_NAME + "_1", new String[]{"AA"});
        requestFieldValues.put(FIELD_NAME + "_2", new String[]{"22"});
        requestFieldValues.put(FIELD_NAME + "_3", new String[]{"33"});

        boolean validationResult = sortCodeValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(false)));
    }

    @Test
    public void givenValidSortCodeAsInputThenShouldReturnTrue() {
        requestFieldValues.put(FIELD_NAME + "_1", new String[]{"11"});
        requestFieldValues.put(FIELD_NAME + "_2", new String[]{"22"});
        requestFieldValues.put(FIELD_NAME + "_3", new String[]{"33"});

        boolean validationResult = sortCodeValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(true)));
    }
}
