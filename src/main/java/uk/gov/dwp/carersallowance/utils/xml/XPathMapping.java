package uk.gov.dwp.carersallowance.utils.xml;

import org.apache.commons.lang3.ObjectUtils;

public class XPathMapping implements Comparable<XPathMapping> {
    private String xpath;
    private String value;
    private String processingInstruction;

    public XPathMapping(String value, String xpath, String processingInstruction) {
        this.xpath = xpath;
        this.value = value;
        this.processingInstruction = processingInstruction;
    }

    public String getXpath()                 { return xpath; }
    public String getValue()                 { return value; }
    public String getProcessingInstruction() { return processingInstruction; }

    /**
     * Compare using xpath field
     * @param other
     * @return -ve this < other
     *          0  this = other
     *         +ve this > other
     */
    @Override
    public int compareTo(XPathMapping other) {
        if(other == this) {
            return 0;
        }

        if(other == null) {
            return 1;
        }

        return ObjectUtils.compare(this.getXpath(), other.getXpath());
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName()).append("@").append(System.identityHashCode(this));
        buffer.append("=[");
        buffer.append("data = ").append(value);
        buffer.append(", xpath = ").append(xpath);
        buffer.append(", processingInstruction = ").append(processingInstruction);
        buffer.append("]");

        return buffer.toString();
    }
}