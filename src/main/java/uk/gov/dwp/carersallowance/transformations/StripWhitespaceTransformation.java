package uk.gov.dwp.carersallowance.transformations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StripWhitespaceTransformation implements Transformation {
    private static final Logger LOG = LoggerFactory.getLogger(StripWhitespaceTransformation.class);

    @Override
    public String transform(String value) {
        if (value == null) {
            return null;
        }
        char[] dest = new char[value.length()];
        int destIndex = 0;
        for (int srcIndex = 0; srcIndex < value.length(); srcIndex++) {
            char ch = value.charAt(srcIndex);
            if (Character.isWhitespace(ch) == false) {
                dest[destIndex] = ch;
                destIndex++;
            }
        }

        if (destIndex == 0) {
            return "";
        }

        String result = new String(dest, 0, destIndex);
        return result;
    }

    public static void main(String[] args) {
        StripWhitespaceTransformation stripWhitespace = new StripWhitespaceTransformation();

        String[] values = {null, "", "X", " X ", "X ", " X", " X O X ", " X O X", "X O X ", " ", "      "};
        for (String value : values) {
            LOG.debug("'{}'='{}'", value, stripWhitespace.transform(value));
        }
    }
}
