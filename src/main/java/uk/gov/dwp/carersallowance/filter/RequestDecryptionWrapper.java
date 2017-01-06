package uk.gov.dwp.carersallowance.filter;

import uk.gov.dwp.carersallowance.encryption.FieldEncryptionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by peterwhitehead on 06/01/2017.
 */
public class RequestDecryptionWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> allParameters = null;
    private final FieldEncryptionService fieldEncryptionService;

    public RequestDecryptionWrapper(final HttpServletRequest request, final FieldEncryptionService fieldEncryptionService) {
        super(request);
        this.fieldEncryptionService = fieldEncryptionService;
    }

    @Override
    public String getParameter(final String name) {
        String[] strings = getParameterMap().get(name);
        if (strings != null) {
            return strings[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (allParameters == null) {
            allParameters = new TreeMap<>();
            for (String fieldName : super.getParameterMap().keySet()) {
                allParameters.put(fieldEncryptionService.decryptAES(fieldName), super.getParameterValues(fieldName));
            }
        }
        //Return an unmodifiable collection because we need to uphold the interface contract.
        return Collections.unmodifiableMap(allParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(final String name) {
        String[] strings = getParameterMap().get(name);
        if (strings != null) {
            return strings;
        }
        return super.getParameterValues(name);
    }
}
