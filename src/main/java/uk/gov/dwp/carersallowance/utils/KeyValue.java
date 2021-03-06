package uk.gov.dwp.carersallowance.utils;

import org.apache.commons.lang3.StringUtils;

public class KeyValue {
    private String key;
    private String value;

    /**
     * Split key/value around the
     * @param string
     * @param startSeparator
     */
    public KeyValue(String string, String startSeparator) {
        this(string, startSeparator, null);
    }

    public KeyValue(String string, String startSeparator, String endSeparator) {
        if(StringUtils.isBlank(string)) {
            return;
        }

        if(StringUtils.isBlank(startSeparator)) {
            key = string.trim();
            return;
        }

        int pos = string.indexOf(startSeparator);
        if(pos < 0) {
            key = string.trim();
            return;
        }

        key = string.substring(0, pos);
        key = key.trim();
        if(key.equals("")) {
            key = null;
        }

        if(pos < string.length()) {
            value = string.substring(pos + 1);
            value = value.trim();
            if(StringUtils.isEmpty(endSeparator) == false && value.endsWith(endSeparator)) {
                value = value.substring(0, value.length() - endSeparator.length());
                value = value.trim();
            }

            if(value.equals("")) {
                value = null;
            }
        }
    }

    public String getKey()   { return key; }
    public String getValue() { return value; }

    public static String getKey(String string, String seperator) {
        KeyValue keyValue = new KeyValue(string, seperator);
        return keyValue.getKey();
    }

    public static String getValue(String string, String seperator) {
        KeyValue keyValue = new KeyValue(string, seperator);
        return keyValue.getValue();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("key = ").append(key);
        buffer.append(", value = ").append(value);
        buffer.append("]");

        return buffer.toString();
    }

    public static void main(String[] args) {
//        String[] data = {null, "", " ", "=", " = ", "hello=world", " hello = world ", "=world", "hello=", " =world", " hello= ", "hello==world"};
//        for(String string: data) {
//            KeyValue keyValue = new KeyValue(string, "=");
//            System.out.println("string = '" + string + "', key = '" + keyValue.getKey() + "', value = '" + keyValue.getValue() + "'");
//        }

        String[] processingInstructions = {"DWPBody/DWPCATransaction/DWPCAClaim/DateOfClaim/Answer[@type=date]"};
        for(String string: processingInstructions) {
            KeyValue keyValue = new KeyValue(string, "[", "]");
            System.out.println("string = '" + string + "', key = '" + keyValue.getKey() + "', value = '" + keyValue.getValue() + "'");
        }

    }
}