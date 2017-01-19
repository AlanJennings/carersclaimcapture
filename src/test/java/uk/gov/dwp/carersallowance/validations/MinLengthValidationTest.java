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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = C3Application.class)
public class MinLengthValidationTest {

    private MinLengthValidation minLengthValidation;
    private ValidationSummary validationSummary;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TransformationManager transformationManager;

    private final String FIELD_NAME = "minlength";
    private Map<String, String[]> requestFieldValues;
    private Map<String, String[]> existingFieldValues;

    @Before
    public void setup() {
        minLengthValidation = new MinLengthValidation("5");
        validationSummary = new ValidationSummary();
        requestFieldValues = new HashMap<>();
        existingFieldValues = new HashMap<>();
        initMocks(this);
    }

    @Test
    public void givenInputWithLengthLowerThanConditionThenShouldReturnFalse() {
        requestFieldValues.put(FIELD_NAME, new String[]{"1234"});

        boolean validationResult = minLengthValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(false)));
    }

    @Test
    public void givenInputWithLengthMatchingConditionThenShouldReturnTrue() {
        requestFieldValues.put(FIELD_NAME, new String[]{"12345"});

        boolean validationResult = minLengthValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(true)));
    }

    @Test
    public void givenInputWithLengthGreaterThanConditionThenShouldReturnTrue() {
        requestFieldValues.put(FIELD_NAME, new String[]{"123456"});

        boolean validationResult = minLengthValidation.validate(validationSummary, messageSource, transformationManager, FIELD_NAME, requestFieldValues, existingFieldValues);

        assertThat(validationResult, is(equalTo(true)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNonNumericConditionThenShouldThrowAnException() {
        minLengthValidation = new MinLengthValidation("Not a number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenConditionLessThanOneThenShouldThrowException() {
        minLengthValidation = new MinLengthValidation("0");
    }
}
