package uk.gov.dwp.carersallowance.filter;

import org.springframework.stereotype.Component;
import uk.gov.dwp.carersallowance.encryption.FieldEncryptionService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by peterwhitehead on 06/01/2017.
 */
@Component
public class RequestParameterDecryptionFilter implements Filter {
    private final FieldEncryptionService fieldEncryptionService;

    @Inject
    public RequestParameterDecryptionFilter(final FieldEncryptionService fieldEncryptionService) {
        this.fieldEncryptionService = fieldEncryptionService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new RequestDecryptionWrapper((HttpServletRequest)request, fieldEncryptionService), response);
    }

    @Override
    public void destroy() { }
}
