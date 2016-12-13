package uk.gov.dwp.carersallowance.transformations;

public class LowerCaseTransformation implements Transformation {

    @Override
    public String transform(String value) {
        if(value == null) {
            return null;
        }
        return value.toLowerCase();
    }

}
