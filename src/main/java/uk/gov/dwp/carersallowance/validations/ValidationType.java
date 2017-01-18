package uk.gov.dwp.carersallowance.validations;

import java.util.Arrays;

/**
 * In priority order, TODO this appears in AbstractFormController as well, and needs moving out of there
 */
public enum ValidationType {
    MANDATORY("mandatory", "%s.validation.mandatory"),
    MAX_LENGTH("maxLength", "%s.validation.maxlength"),
    DATE("date", "%s.validation.date", "%s.validation.date.lowerlimit", "%s.validation.date.upperlimit"),
    ADDRESS("address", "%s.validation.address"),
    GROUP_ANY("group_any", "%s.validation.group_any"),
    GROUP_ALL("group_all", "%s.validation.group_all"),
    REGEX("regex", "%s.validation.regex"),                              // note blank values are not checked against regex.
    CONFIRM_FIELD("confirm_field", "%s.validation.confirm_field"),
    SORT_CODE("sortcode", "%s.validation.sortcode");

    private String   property;
    private String   keyFormat;
    private String[] additionalParamKeyFormats;

    private ValidationType(String property, String keyFormat, String... additionalParamKeyFormats) {
        this.property = property;
        this.keyFormat = keyFormat;
        this.additionalParamKeyFormats = additionalParamKeyFormats;
    }

    public String   getProperty()                  { return property; }
    public String   getKeyFormat()                 { return keyFormat; }
    public String[] getAdditionalParamKeyFormats() { return additionalParamKeyFormats; }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("property = ").append(property);
        buffer.append(", keyFormat = ").append(keyFormat);
        buffer.append(", additionalParamKeyFormats = ").append(additionalParamKeyFormats == null ? null : Arrays.asList(additionalParamKeyFormats));
        buffer.append("]");

        return buffer.toString();
    }

    public static void main(String[] args) {
        for(ValidationType type: ValidationType.values()) {
            System.out.println("formatting: '" + type.getKeyFormat() + "'");
            String key = String.format(type.getKeyFormat(), "'args'");
            System.out.println("key = '" + key + "'");
        }
    }

}