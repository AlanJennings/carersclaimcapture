package uk.gov.dwp.carersallowance.validations;

/**
 * In priority order, TODO this appears in AbstractFormController as well, and needs moving out of there
 */
public enum ValidationType {
    MANDATORY("mandatory", "%.validation.mandatory"),
    MAX_LENGTH("maxLength", "%.validation.maxlength"),
    DATE("date", "%.validation.date"),
    ADDRESS("address", "%.validation.address"),
    GROUP_ANY("group_any", "%.validation.group_any"),
    GROUP_ALL("group_all", "%.validation.group_all"),
    REGEX("regex", "%.validation.regex"),
    CONFIRM_FIELD("confirm_field", "%.validation.confirm_field");

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

}