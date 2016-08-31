package uk.gov.dwp.carersallowance.utils.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.dwp.carersallowance.utils.Parameters;


/**
 * Validate the XML against some schemas
 *
 * @author David Hutchinson (drh@elegantsolutions.co.uk)
 */
public class XmlValidator {
    private static final Logger LOG = LoggerFactory.getLogger(XmlValidator.class);

    private Schema schema;

    public XmlValidator(Source[] schemas) throws SAXException {
        Parameters.validateMandatoryArgs(schemas, "schemas");
        LOG.trace("Started XmlValidator.XmlValidator");
        LOG.debug("schemas = {}", Arrays.asList(schemas));
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = schemaFactory.newSchema(schemas);
        } catch(SAXException e) {
            LOG.error("Unable to process validation schemas", e);
            throw new SAXException("Unable to process validation schemas", e);
        } finally {
            LOG.trace("Ending XmlValidator.XmlValidator");
        }
    }

    public ValidationErorrs validate(Source xml) throws SAXException, IOException {
        LOG.trace("Started XmlValidator.validate");
        try {
            Validator validator = schema.newValidator();
            XmlErrorHandler errorHandler = new XmlErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(xml);

            if(errorHandler.getErrors().isEmpty()
            && errorHandler.getFatalErrors().isEmpty()
            && errorHandler.getWarnings().isEmpty())
                return null;

            return errorHandler;
        } finally {
            LOG.trace("Ending XmlValidator.validate");
        }
    }

    public static class XmlErrorHandler extends DefaultHandler implements ValidationErorrs {
        private List<SAXParseException> fatalErrors;
        private List<SAXParseException> errors;
        private List<SAXParseException> warnings;

        public XmlErrorHandler() {
            fatalErrors = new ArrayList<SAXParseException>();
            errors = new ArrayList<SAXParseException>();
            warnings = new ArrayList<SAXParseException>();
        }

        public List<SAXParseException> getFatalErrors() { return fatalErrors; }
        public List<SAXParseException> getErrors()      { return errors; }
        public List<SAXParseException> getWarnings()    { return warnings; }

        public void error(SAXParseException exception) {
            errors.add(exception);
        }

        public void fatalError(SAXParseException exception) {
            fatalErrors.add(exception);
        }

        public void warning(SAXParseException exception) {
            warnings.add(exception);
        }

        public boolean isValid() {
            return fatalErrors.isEmpty() && errors.isEmpty();
        }
    }

    public interface ValidationErorrs {
        public boolean isValid();

        public List<SAXParseException> getFatalErrors();
        public List<SAXParseException> getErrors();
        public List<SAXParseException> getWarnings();
    }


    public static ValidationErorrs validateDc(String filename) throws SAXException, IOException {
        System.out.println(filename);

        String xmlSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\xml.xsd";
        String dcSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\simpledc20021212.xsd";
        String oaiDcSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\oai_dc.xsd";

        Source xmlFile = new StreamSource(new File(filename));
        Source xmlSchema = new StreamSource(new File(xmlSchemaFile));
        Source dcSchema = new StreamSource(new File(dcSchemaFile));
        Source oaiDcSchema = new StreamSource(new File(oaiDcSchemaFile));

        // Note: dependencies must come first
        XmlValidator xmlValidator = new XmlValidator(new Source[]{xmlSchema, dcSchema, oaiDcSchema});
        ValidationErorrs errors = xmlValidator.validate(xmlFile);
        return errors;
    }

    public static ValidationErorrs validateMods(String filename) throws SAXException, IOException {
        System.out.println(filename);

        String modsSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\mods-3-4.xsd";
        String xlinkSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\xlink.xsd";
        String xmlSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\xml.xsd";

        Source xmlFile = new StreamSource(new File(filename));
        Source modsSchema = new StreamSource(new File(modsSchemaFile));
        Source xlinkSchema = new StreamSource(new File(xlinkSchemaFile));
        Source xmlSchema = new StreamSource(new File(xmlSchemaFile));

        // Note: dependencies must come first
        XmlValidator xmlValidator = new XmlValidator(new Source[]{xmlSchema, xlinkSchema, modsSchema});
        ValidationErorrs errors = xmlValidator.validate(xmlFile);
        return errors;
    }

    public static ValidationErorrs validateMarc21Slim(String filename) throws SAXException, IOException {
        System.out.println(filename);

        String marcSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\MARC21slim.xsd";

        Source xmlFile = new StreamSource(new File(filename));
        Source marcSchema = new StreamSource(new File(marcSchemaFile));

        // Note: dependencies must come first
        XmlValidator xmlValidator = new XmlValidator(new Source[]{marcSchema});
        ValidationErorrs errors = xmlValidator.validate(xmlFile);
        return errors;
    }

    public static ValidationErorrs validateOaiMarc(String filename) throws SAXException, IOException {
        System.out.println(filename);

        String oaiMarcSchemaFile = "C:\\projects\\EIS-007\\metadata aggregator\\documents\\schema\\oai_marc.xsd";

        Source xmlFile = new StreamSource(new File(filename));
        Source oaiMarcSchema = new StreamSource(new File(oaiMarcSchemaFile));

        // Note: dependencies must come first
        XmlValidator xmlValidator = new XmlValidator(new Source[]{oaiMarcSchema});
        ValidationErorrs errors = xmlValidator.validate(xmlFile);
        return errors;
    }

    private static void printValidationErrors(ValidationErorrs errors) {
        if(errors == null) {
            System.out.println("VALID");
        } else {
            if(errors.getFatalErrors().isEmpty() == false) {
                System.out.println("FATAL ERROR");
                for(SAXParseException fatalError : errors.getFatalErrors()) {
                    System.out.println(fatalError.getMessage());
                }
                System.out.println();
            }

            if(errors.getErrors().isEmpty() == false) {
                System.out.println("ERROR");
                for(SAXParseException error : errors.getErrors()) {
                    System.out.println(error.getMessage());
                }
                System.out.println();
            }

            if(errors.getWarnings().isEmpty() == false) {
                System.out.println("WARNING");
                for(SAXParseException warning : errors.getWarnings()) {
                    System.out.println(warning.getMessage());
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException, SAXException {
        System.out.println("Started");

//        printValidationErrors(validateOaiMarc("C:\\projects\\EIS-007\\metadata aggregator\\aleph\\sample.aleph.xml"));
//
//        printValidationErrors(validateMarc21Slim("C:\\projects\\EIS-007\\metadata aggregator\\aleph\\sample.aleph.marc21slim.xml"));
//
//        printValidationErrors(validateMods("C:\\projects\\EIS-007\\metadata aggregator\\aleph\\sample.mer.mods.xml"));    // valid
//
//        printValidationErrors(validateMods("C:\\projects\\EIS-007\\metadata aggregator\\aleph\\sample.aleph.mods.xml"));  // valid
//
//        printValidationErrors(validateMods("C:\\projects\\EIS-007\\metadata aggregator\\aleph\\sample.combined.mods.xml"));

        printValidationErrors(validateDc("C:\\projects\\EIS-007\\metadata aggregator\\aleph\\test\\sample.aleph.dc.xml"));

        System.out.println("Complete");
    }
}
