package uk.gov.dwp.carersallowance.transformations;

public enum TransformationType {
//    carerNationalInsuranceNumber.validation.transformations          = uppercase, stripwhitespace   # transform the data for validation only
//    carerNationalInsuranceNumber.transformations                     = uppercase, stripwhitespace   # transform the data permanantly
//    carerNationalInsuranceNumber.completed.transformations           = uppercase, stripwhitespace   # transform the data once the claim is complete

    UPPERCASE("uppercase", new UpperCaseTransformation()),
    LOWERCASE("lowercase", new LowerCaseTransformation()),
    STRIPWHITESPACE("stripwhitespace", new StripWhitespaceTransformation());

    private String         name;
    private Transformation transformation;

    private TransformationType(String name, Transformation transformation) {
        this.name = name;
        this.transformation = transformation;
    }

    public String         getName()             { return name; }
    public Transformation getTransformation()   { return transformation; }

    public static TransformationType valueOfName(String property) {
        for(TransformationType type : TransformationType.values()) {
            if(type.getName().equalsIgnoreCase(property)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown type: " + property + ", expecting one of: 'uppercase', 'lowercase', 'stripwhitespace'");
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("name = ").append(name);
        buffer.append(", transformation = ").append(transformation);
        buffer.append("]");

        return buffer.toString();
    }
}