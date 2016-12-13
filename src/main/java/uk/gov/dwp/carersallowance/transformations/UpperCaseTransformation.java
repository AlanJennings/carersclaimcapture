package uk.gov.dwp.carersallowance.transformations;

public class UpperCaseTransformation implements Transformation {

    @Override
    public String transform(String value) {
        if(value == null) {
            return null;
        }
        return value.toUpperCase();
    }

}
