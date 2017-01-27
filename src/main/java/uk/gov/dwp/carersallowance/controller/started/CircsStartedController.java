package uk.gov.dwp.carersallowance.controller.started;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dwp.carersallowance.controller.defaultcontoller.DefaultChangeOfCircsController;
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
public class CircsStartedController {
    private static final Logger LOG = LoggerFactory.getLogger(CircsStartedController.class);

    private final DefaultChangeOfCircsController defaultChangeOfCircsController;
    private final Boolean circsReplicaEnabledProperty;
    private final String circsReplicaDataFileProperty;
    private final String originTag;
    private final URL xmlMappingFile;
    private final ChangeLanguageProcess changeLanguageProcess;
    private final SessionManager sessionManager;

    private static final String CURRENT_CIRCS_PAGE = "/circumstances/report-changes/selection";
    private static final String CHANGE_SELECTION_PAGE = "/circumstances/report-changes/change-selection";
    private static final String ORIGIN_NI = "GB-NIR";

    @Inject
    public CircsStartedController(final DefaultChangeOfCircsController defaultChangeOfCircsController,
                                  @Value("${xml.circsMappingFile}") String mappingFile,
                                  @Value("${circs.replica.enabled}") final Boolean circsReplicaEnabledProperty,
                                  @Value("${circs.replica.datafile}") final String circsReplicaDataFileProperty,
                                  @Value("${origin.tag}") final String originTag,
                                  final ChangeLanguageProcess changeLanguageProcess,
                                  final SessionManager sessionManager) {
        this.xmlMappingFile = XmlClaimReader.class.getClassLoader().getResource(mappingFile);
        this.defaultChangeOfCircsController = defaultChangeOfCircsController;
        this.circsReplicaEnabledProperty = circsReplicaEnabledProperty;
        this.circsReplicaDataFileProperty = circsReplicaDataFileProperty;
        this.originTag = originTag;
        this.changeLanguageProcess = changeLanguageProcess;
        this.sessionManager = sessionManager;
    }

    @RequestMapping(value = CURRENT_CIRCS_PAGE, method = RequestMethod.POST)
    public String postCircsForm(final HttpServletRequest request, final Model model) {
        return defaultChangeOfCircsController.postForm(request, model);
    }

    @RequestMapping(value = CURRENT_CIRCS_PAGE, method = RequestMethod.GET)
    public String showCircsForm(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        LOG.info("circsReplicaEnabledProperty = " + circsReplicaEnabledProperty);
        LOG.info("circsReplicaDataFileProperty = " + circsReplicaDataFileProperty);

        if (request.getQueryString() == null || !request.getQueryString().contains("changing=true")) {
            changeLanguageProcess.processChangeLanguage(request, response);
            sessionManager.createSessionVariables(request, response, getReplicateDataFile(), xmlMappingFile, C3Constants.CIRCS);
        }
        if (ORIGIN_NI.equals(originTag)) {
            defaultChangeOfCircsController.getForm(request, model);
            return CURRENT_CIRCS_PAGE;
        }
        return "redirect:" + CHANGE_SELECTION_PAGE;
    }

    private String getReplicateDataFile() {
        if (circsReplicaEnabledProperty) {
            return circsReplicaDataFileProperty;
        }
        return null;
    }
}
