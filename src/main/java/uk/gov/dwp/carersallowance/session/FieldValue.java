package uk.gov.dwp.carersallowance.session;

import java.util.Arrays;

public class FieldValue {
    private String[] values;

    public FieldValue(String value) {
        if(value == null) {
            values = null;
        } else {
            values = new String[]{value};
        }
    }

    public FieldValue(String[] values) {
        this.values = values;
    }

    public String getSingleValue() {
        if(values == null) {
            return null;
        }

        if(values.length == 1) {
            return values[0];
        }

        throw new MultipleValuesException("Expected a single value, but found multiple values: " + Arrays.asList(values));
    }

    public String[] getValues() {
        return values;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        if(values == null) {
            buffer.append("values = null");
        } else {
            buffer.append("values = (");
            for(int index = 0; index < values.length; index++) {
                if(index == 0) {
                    buffer.append(", ");
                }
                buffer.append(values[index]);
            }
            buffer.append(")");
        }
        buffer.append("]");

        return buffer.toString();
    }
}