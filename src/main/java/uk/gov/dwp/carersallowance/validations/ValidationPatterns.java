package uk.gov.dwp.carersallowance.validations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationPatterns {

    public static final int    ACCOUNT_HOLDER_NAME_MAX_LENGTH = 60;
    public static final int    ACCOUNT_NUMBER_MAX_LENGTH      = 10;
    public static final int    ACCOUNT_NUMBER_MIN_LENGTH      = 6;
    public static final int    CURRENCY_REGEX_MAX_LENGTH      = 12;
    public static final int    FULL_NAME_MAX_LENGTH           = 60;
    public static final int    NAME_MAX_LENGTH                = 35;
    public static final int    NATIONALITY_MAX_LENGTH         = 35;
    public static final int    PHONE_NUMBER_MAX_LENGTH        = 20;

    public static final String RESTRICTED_CHARS               = "^[A-Za-zÀ-ƶ\\s0-9\\(\\)&£€\\\"\\'!\\-_:;\\.,/\\?\\@]*$";
    public static final String PHONE_NUMBER_SEQ               = "^[0-9 \\-]$";
    public static final String NATIONALITY_SEQ                = "^[a-zA-ZÀ-ƶ \\-\\']$";

    public static final String ACCOUNT_HOLDER_NAME_REGEX      = "^[" + RESTRICTED_CHARS + "]{1," + ACCOUNT_HOLDER_NAME_MAX_LENGTH + "}$$";
    public static final String ACCOUNT_NUMBER_REGEX           = "^\\d{" + ACCOUNT_NUMBER_MIN_LENGTH + "," + ACCOUNT_NUMBER_MAX_LENGTH + "}$$";
    public static final String CURRENCY_REGEX                 = "^\\£?[0-9]{1,8}(\\.[0-9]{1,2})?$";
    public static final String DECIMAL_REGEX                  = "^[0-9]{1,12}(\\.[0-9]{1,2})?$";
    public static final String EMAIL_REGEX                    = "^[a-zA-Z0-9\\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])*(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9]))+$";
    public static final String FULL_NAME_REGEX                = "^[" + RESTRICTED_CHARS + "]{1," + FULL_NAME_MAX_LENGTH + "}$$";
    public static final String NATIONALITY_REGEX              = "^[" + NATIONALITY_SEQ + "]{1," + NATIONALITY_MAX_LENGTH + "}$$";
    public static final String NINO_REGEX                     = "(([A-CEHJ-MOPRSW-Y]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6})|([G]{1}[ACEGHJ-NPR-TW-Z]{1}[0-9]{6})|([N]{1}[A-CEGHJL-NPR-TW-Z]{1}[0-9]{6})|([T]{1}[A-CEGHJ-MPR-TW-Z]{1}[0-9]{6})|([Z]{1}[A-CEGHJ-NPR-TW-Y]{1}[0-9]{6}))(([A-D ]{1})|([\\S\\s\\d]{0,0}))";
    public static final String NUMBER_OR_SPACE_REGEX          = "^[0-9 ]*$";
    public static final String NUMBER_REGEX                   = "^[0-9]*$";
    public static final String PHONE_NUMBER_REGEX             = "^[" + PHONE_NUMBER_SEQ + "]{7," + PHONE_NUMBER_MAX_LENGTH + "}$$";
    public static final String POSTCODE_REGEX                 = "^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) ?[0-9][A-Z]{2})$";
    public static final String RESTRICTED_POSTCODE_REGEX      = "^[A-Za-zÀ-ƶ\\s0-9]*$";
    public static final String SORT_CODE_REGEX                = "^\\d{6}$";
    public static final String SURNAME_REGEX                  = "^[" + RESTRICTED_CHARS + "]{1," + NAME_MAX_LENGTH + "}$$";

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = ValidationPatterns.class.getFields();
        System.out.println("Fields = " + Arrays.asList(fields));
        for(Field field: fields) {
            String fieldName = field.getName();
            if(field.getType() != String.class) {
                System.out.println("field: " + field + " is not a string");
            } else {
                System.out.print("pattern: " + fieldName);
                String value = (String)field.get(null);
                Pattern pattern = Pattern.compile(value);
                Matcher matcher = pattern.matcher("");
                System.out.println(" = " + matcher.matches());
            }
        }
    }

    /*
      The following are valid when empty:

        pattern: RESTRICTED_CHARS = true
        pattern: NUMBER_OR_SPACE_REGEX = true
        pattern: NUMBER_REGEX = true
        pattern: RESTRICTED_POSTCODE_REGEX = true

     The following are NOT valid when empty:

        pattern: PHONE_NUMBER_SEQ = false
        pattern: NATIONALITY_SEQ = false
        pattern: ACCOUNT_HOLDER_NAME_REGEX = false
        pattern: ACCOUNT_NUMBER_REGEX = false
        pattern: CURRENCY_REGEX = false
        pattern: DECIMAL_REGEX = false
        pattern: EMAIL_REGEX = false
        pattern: FULL_NAME_REGEX = false
        pattern: NATIONALITY_REGEX = false
        pattern: NINO_REGEX = false
        pattern: PHONE_NUMBER_REGEX = false
        pattern: POSTCODE_REGEX = false
        pattern: SORT_CODE_REGEX = false
        pattern: SURNAME_REGEX = false

     */
}
