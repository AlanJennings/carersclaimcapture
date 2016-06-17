package uk.gov.dwp.carersallowance.session;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.gov.dwp.carersallowance.utils.Parameters;

public class FieldCollection {

    public static Map<String, String> getFieldValues(Map<String, String[]> allFieldValues, String[] fieldNames) {
        Parameters.validateMandatoryArgs(allFieldValues, "allFieldValues");
        if(fieldNames == null) {
            return null;
        }

        Map<String, String> fieldValues;
        fieldValues = new HashMap<>();
        for(String name: fieldNames) {
            String[] values = allFieldValues.get(name);
            if(values == null) {
                fieldValues.put(name, null);
            } else if(values.length == 1) {
                fieldValues.put(name, values[0]);
            } else {
                throw new MultipleValuesException("" + name + " has multiple values: " + Arrays.asList(values));
            }
        }

        return fieldValues;
    }

    public static Map<String, String> getFieldCollection(List<Map<String, String>> list, String idFieldName, String idFieldValue) {
        Parameters.validateMandatoryArgs(new Object[]{list, idFieldName, idFieldValue}, new String[]{"list", "idFieldName", "idFieldValue"});
        for(Map<String, String> map: list) {
            if(idFieldValue.equals(map.get(idFieldName))) {
                return map;
            }
        }

        return null;
    }

    /**
     * Not sure if this would be better using a separate reversing comparator
     */
    public static class FieldCollectionComparator implements Comparator<Map<String, String>>{

        private boolean      reverse;
        private List<String> compareFields;

        public FieldCollectionComparator(String... compareFields) {
            this(false, compareFields);
        }

        public FieldCollectionComparator(boolean reverse, String... compareFields) {
            Parameters.validateMandatoryArgs(compareFields, "compareFields");
            if(compareFields.length == 0) {
                throw new IllegalArgumentException("no compareFields specified");
            }

            this.compareFields = Arrays.asList(compareFields);
        }

        /**
         * @return -ve first < second
         *          0  first = second
         *         +ve first > second
         */
        @Override
        public int compare(Map<String, String> first, Map<String, String> second) {
            int result = 0;

            if(first == second) {
                result = 0;
            } else if(first == null) {
                result = -1;
            } else if(second == null) {
                result = 1;
            } else {
                for(String fieldName: compareFields) {
                    String firstValue = first.get(fieldName);
                    String secondValue = second.get(fieldName);
                    int compareResult = compare(firstValue, secondValue);
                    if(compareResult != 0) {
                        result = compareResult;
                        break;
                    }
                }
            }

            if(reverse) {
                return -result;
            }
            return result;
        }

        /**
         * @return -ve first < second
         *          0  first = second
         *         +ve first > second
         */
        private int compare(String first, String second) {
            if(first == second) {
                return 0;
            }

            if(first == null) {
                return -1;
            }

            return first.compareTo(second);
        }
    }
}
