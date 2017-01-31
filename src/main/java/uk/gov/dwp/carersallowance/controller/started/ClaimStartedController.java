package uk.gov.dwp.carersallowance.controller.started;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultFormController;
import uk.gov.dwp.carersallowance.session.SessionManager;
import uk.gov.dwp.carersallowance.utils.C3Constants;
import uk.gov.dwp.carersallowance.xml.XmlClaimReader;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * Created by peterwhitehead on 22/12/2016.
 */
@Controller
public class ClaimStartedController {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimStartedController.class);

    private final Boolean replicaEnabledProperty;
    private final String replicaDataFileProperty;
    private final DefaultFormController defaultFormController;
    private final URL xmlMappingFile;
    private final ChangeLanguageProcess changeLanguageProcess;
    private final SessionManager sessionManager;

    private static final String CURRENT_PAGE = "/allowance/benefits";

    @Inject
    public ClaimStartedController(final @Value("${replica.enabled}") Boolean replicaEnabledProperty,
                                  final @Value("${replica.datafile}") String replicaDataFileProperty,
                                  final DefaultFormController defaultFormController,
                                  final @Value("${xml.mappingFile}") String mappingFile,
                                  final ChangeLanguageProcess changeLanguageProcess,
                                  final SessionManager sessionManager) {
        this.replicaEnabledProperty = replicaEnabledProperty;
        this.replicaDataFileProperty = replicaDataFileProperty;
        this.defaultFormController = defaultFormController;
        this.changeLanguageProcess = changeLanguageProcess;
        this.sessionManager = sessionManager;
        this.xmlMappingFile = XmlClaimReader.class.getClassLoader().getResource(mappingFile);
    }

    @RequestMapping(value = CURRENT_PAGE, method = RequestMethod.GET)
    public String getForm(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        if (request.getQueryString() == null || !request.getQueryString().contains("changing=true")) {
            changeLanguageProcess.processChangeLanguage(request, response);
            sessionManager.createSessionVariables(request, response, getReplicateDataFile(), xmlMappingFile, C3Constants.CLAIM);
        }
        defaultFormController.getForm(request, model);
        return CURRENT_PAGE;
    }

    @RequestMapping(value = CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(final HttpServletRequest request, final Model model) {
        return defaultFormController.postForm(request, null, null, model);
    }

    private String getReplicateDataFile() {
        if (replicaEnabledProperty) {
            return replicaDataFileProperty;
        }
        return null;
    }
}
