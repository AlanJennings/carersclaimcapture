package uk.gov.dwp.carersallowance.utils.xml;

import org.apache.commons.lang3.ObjectUtils;

public class XPathMapping implements Comparable<XPathMapping> {
    private String xpath;
    private String value;

    public XPathMapping(String value, String xpath) {
        this.xpath = xpath;
        this.value = value;
    }

    public String getXpath() { return xpath; }
    public String getValue() { return value; }

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
        buffer.append("]");

        return buffer.toString();
    }
}