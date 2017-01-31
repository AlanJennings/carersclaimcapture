package uk.gov.dwp.carersallowance.controller.preview;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.dwp.carersallowance.controller.AbstractFormController;

import uk.gov.dwp.carersallowance.controller.PageOrder;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.transformations.TransformationManager;
import uk.gov.dwp.carersallowance.utils.LoadFile;

@Controller
public class PreviewController extends AbstractFormController {
    private static final Logger LOG = LoggerFactory.getLogger(PreviewController.class);

    private static final String CURRENT_PAGE  = "/preview";
    private static final String REDIRECT_PAGE = "/preview/redirect/{redirectTo}";
    private static final String PREVIEW_MAPPING_CLAIM = "preview.mappings.claim";
    private final PreviewPageProcessing previewPageProcessing;
    private final Map<String, PreviewMapping> previewMappings;

    @Autowired
    public PreviewController(final SessionManager sessionManager,
                             final MessageSource messageSource,
                             final TransformationManager transformationManager,
                             final PreviewPageProcessing previewPageProcessing,
                             final PageOrder pageOrder) {
        super(sessionManager, messageSource, transformationManager, pageOrder);
        this.previewPageProcessing = previewPageProcessing;
        this.previewMappings = loadPreviewMappings();
    }

    private Map<String, PreviewMapping> loadPreviewMappings() {
        try {
            final Map<String, PreviewMapping> mappings = new HashMap<>();
            final URL claimTemplateUrl = this.getClass().getClassLoader().getResource(PREVIEW_MAPPING_CLAIM);
            final List<String> previewMappings = LoadFile.readLines(claimTemplateUrl);
            for (final String mapping : previewMappings) {
                if (StringUtils.isNotEmpty(mapping)) {
                    String[] mappingValue = mapping.split("=");
                    mappings.put(mappingValue[0].trim(), new PreviewMapping(mappingValue[1]));
                }
            }
            return mappings;
        } catch (IOException ioe) {
            LOG.error("Failed to load preview mapping file.", ioe);
            throw new RuntimeException(ioe);
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(final HttpServletRequest request, final Model model) {
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        session.setAttribute("beenInPreview", true);
        setReturnToSummaryHash(session, model);
        previewPageProcessing.createParametersForPreviewPage(model, session);
        sessionManager.saveSession(session);
        return super.getForm(request, model);
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, Model model) {
        return super.postForm(request, null, null, model);
    }

    @RequestMapping(value=REDIRECT_PAGE, method = RequestMethod.GET)
    public String redirect(@PathVariable final String redirectTo, final HttpServletRequest request) {
        final Session session = sessionManager.getSession(sessionManager.getSessionIdFromCookie(request));
        session.setAttribute("returnToSummary", redirectTo);
        sessionManager.saveSession(session);
        return createRedirection(redirectTo);
    }

    private void setReturnToSummaryHash(final Session session, final Model model) {
        final String returnToSummaryHash = (String)session.getAttribute("returnToSummary");
        if (StringUtils.isEmpty(returnToSummaryHash)) {
            return;
        }
        model.addAttribute("hash", returnToSummaryHash);
        session.removeAttribute("returnToSummary");
    }

    private String createRedirection(final String redirectTo) {
        final PreviewMapping previewMapping = previewMappings.get(redirectTo);
        return "redirect:" + previewMapping.getUrl() + "#" + previewMapping.getHash() + "_label";
    }
}
