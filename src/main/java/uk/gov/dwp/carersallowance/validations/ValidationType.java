package uk.gov.dwp.carersallowance.validations;

/**
 * In priority order, TODO this appears in AbstractFormController as well, and needs moving out of there
 */
public enum ValidationType {
    MANDATORY("mandatory", "%s.validation.mandatory"),
    MAX_LENGTH("maxLength", "%s.validation.maxlength"),
    DATE_MANDATORY("date", "%s.validation.date.mandatory"),             // These have to handle mandatory as they are multi-field validations
    DATE_OPTIONAL("date", "%s.validation.date.optional"),               // These have to handle mandatory as they are multi-field validations
    ADDRESS_MANDATORY("address", "%s.validation.address.mandatory"),    // These have to handle mandatory as they are multi-field validations
    ADDRESS_OPTIONAL("address", "%s.validation.address.optional"),      // These have to handle mandatory as they are multi-field validations
    GROUP_ANY("group_any", "%s.validation.group_any"),
    GROUP_ALL("group_all", "%s.validation.group_all"),
    REGEX("regex", "%s.validation.regex"),                              // note blank values are not checked against regex.
    CONFIRM_FIELD("confirm_field", "%s.validation.confirm_field");

    private String property;
    private String keyFormat;

    private ValidationType(String property, String keyFormat) {
        this.property = property;
        this.keyFormat = keyFormat;
    }

    public String getProperty()  { return property; }
    public String getKeyFormat() { return keyFormat; }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("property = ").append(property);
        buffer.append(", keyFormat = ").append(keyFormat);
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