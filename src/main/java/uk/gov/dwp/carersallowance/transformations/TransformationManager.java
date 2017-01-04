package uk.gov.dwp.carersallowance.transformations;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import uk.gov.dwp.carersallowance.utils.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by peterwhitehead on 29/12/2016.
 */
@Service
public class TransformationManager {
    private static final Logger LOG = LoggerFactory.getLogger(TransformationManager.class);

    private List<Transformation> getTransformations(final String transformationsKey, final MessageSource messageSource) {
        LOG.trace("Started AbstractFormController.getTransformations");
        try {
            LOG.debug("transformationsKey = {}", transformationsKey);
            if(StringUtils.isBlank(transformationsKey)) {
                return null;
            }

            List<Transformation> list = new ArrayList<>();
            String transformationList = messageSource.getMessage(transformationsKey, null, null, Locale.getDefault());
            if(transformationList != null) {
                String[] transformationNames = transformationList.split(",");
                for(String transformationName : transformationNames ) {
                    transformationName = transformationName.trim();
                    LOG.debug("transformationName = '{}'", transformationName);
                    TransformationType type = TransformationType.valueOfName(transformationName);
                    list.add(type.getTransformation());
                }
            }

            LOG.debug("list = {}", list);
            return list;
        } catch(RuntimeException e) {
            LOG.error("Problems getting transformations for " + transformationsKey, e);
            throw e;
        } finally {
            LOG.trace("Ending AbstractFormController.getTransformations");
        }
    }

    private Object applySessionTransformations(final String fieldName, final Object fieldValue, final List<Transformation> transformations) {
        Parameters.validateMandatoryArgs(fieldName,  "fieldName");
        if (fieldValue == null) {
            return null;
        }

        if (fieldValue instanceof String[]) {
            String[] fieldValues = (String[])fieldValue;
            String[] transformedValues = new String[fieldValues.length];
            for (int index = 0; index < fieldValues.length; index++) {
                Object value = applySessionTransformations(fieldName, fieldValues[index], transformations);
                if (value == null || value instanceof String) {
                    transformedValues[index] = (String)value;
                } else {
                    throw new IllegalArgumentException("Expected value of type String when transforming " + fieldName + ", but received " + value.getClass().getName());
                }
            }
            return transformedValues;
        }

        if ((fieldValue instanceof String) == false) {
            throw new IllegalArgumentException("Expected fieldValue of type String when transforming " + fieldName + ", but received " + fieldValue.getClass().getName());
        }

        String value = (String)fieldValue;
        for (Transformation transformation: transformations) {
            value = transformation.transform(value);
        }
        return value;
    }

    public Object getTransformedValue(final String fieldName, final Object fieldValue, final String transformation, final MessageSource messageSource) {
        String transformationsKey = String.format(transformation, fieldName);
        List<Transformation> transformations = getTransformations(transformationsKey, messageSource);
        return applySessionTransformations(fieldName, fieldValue, transformations);
    }
}
