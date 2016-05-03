package uk.gov.dwp.carersallowance.data;

public class AbstractQuestion<T> implements Question<T> {
    private String id;
    private String label;
    private String hintBefore;
    private String hintAfter;
    private T      value;

    public AbstractQuestion(String id, String label, String hintBefore, String hintAfter, T value) {
        this.id = id;
        this.label = label;
        this.hintBefore = hintBefore;
        this.hintAfter = hintAfter;
        this.value = value;
    }

    public String getId()          { return id; }
    public String getLabel()       { return label; }
    public String getHintBefore()  { return hintBefore; }
    public String getHintAfter()   { return hintAfter; }
    public T      getValue()       { return value; }

    public void setValue(T value) { this.value = value; }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("id = ").append(id);
        buffer.append(", label = ").append(label);
        buffer.append(", hintBefore = ").append(hintBefore);
        buffer.append(", hintAfter = ").append(hintAfter);
        buffer.append(", value = ").append(value);
        buffer.append("]");

        return buffer.toString();
    }
}
