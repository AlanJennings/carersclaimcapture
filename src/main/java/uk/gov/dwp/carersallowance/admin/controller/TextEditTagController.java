package uk.gov.dwp.carersallowance.admin.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import uk.gov.dwp.carersallowance.session.MultipleValuesException;

@Controller
public class TextEditTagController {
    private static final Logger LOG = LoggerFactory.getLogger(TextEditTagController.class);

    private static final String CURRENT_PAGE  = "/admin-interface/textedit.tag";

    private ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    public TextEditTagController(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.GET)
    public String showForm(HttpServletRequest request,
                           Model model,
                           @RequestParam(value = "name", required = false) String name) {
        LOG.trace("Starting AbstractFormController.showFormInternal");
        try {
            LOG.debug("model = {}", model);

            model.addAttribute("name", name);

            // optional values
            model.addAttribute("tagNested", getRequestValue(request, "tagNested"));
            model.addAttribute("outerClass", getRequestValue(request, "outerClass"));
            model.addAttribute("outerStyle", getRequestValue(request, "outerStyle"));
            model.addAttribute("labelKey", getRequestValue(request, "labelKey"));
            model.addAttribute("labelKeyArgs", getRequestValue(request, "labelKeyArgs"));
            model.addAttribute("hintBeforeKey", getRequestValue(request, "hintBeforeKey"));
            model.addAttribute("hintAfterKey", getRequestValue(request, "hintAfterKey"));
            model.addAttribute("value", getRequestValue(request, "value"));
            model.addAttribute("useRawValue", getRequestValue(request, "useRawValue"));
            model.addAttribute("maxLength", getRequestValue(request, "maxLength"));
            model.addAttribute("additionalClasses", getRequestValue(request, "additionalClasses"));

            return CURRENT_PAGE;
        } catch(RuntimeException e) {
            LOG.error("Unexpected RuntimeException", e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.showFormInternal");
        }
    }

    private String getRequestValue(HttpServletRequest request, String paramName) {
        String[]  values = request.getParameterValues(paramName);
        if(values == null || values.length == 0) {
            return null;
        }
        if(values.length == 1) {
            return values[0];
        }

        throw new MultipleValuesException("request parameter(" + paramName + ") does not have a single value: " + Arrays.asList(values));
    }

    private String getMessageKey(String code, String parentName, String element) {
        if(StringUtils.isEmpty(code)) {
            return parentName + "." + element;
        }
        return code;
    }

    private String getMessage(String code, String parentName, String element) {
        try {
            if(StringUtils.isEmpty(code) == false) {
                return messageSource.getMessage(code, null, null, null);
            } else if(StringUtils.isEmpty(parentName) == false && StringUtils.isEmpty(element) == false) {
                return messageSource.getMessage(parentName + "." + element, null, null, null);
            }
            return null;
        } catch(NoSuchMessageException e) {
            LOG.debug("No message for code: {}, parentName = {}, element = {}", code, parentName, element);
            return null;
        }
    }

    @RequestMapping(value=CURRENT_PAGE, method = RequestMethod.POST)
    public String postForm(HttpServletRequest request, HttpSession session, Model model) {
        return null;
    }
}
