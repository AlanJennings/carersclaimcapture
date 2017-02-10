package uk.gov.dwp.carersallowance.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import uk.gov.dwp.carersallowance.utils.xml.XPathMapping;
import uk.gov.dwp.carersallowance.utils.xml.XPathMappingList;

public class XmlClaimReader extends AbstractXMLReader {
    private static final Logger LOG = LoggerFactory.getLogger(XmlClaimReader.class);

    private String fileSuffix;

    public XmlClaimReader(final String xmlFile, final Boolean sessionVariablesOnly, final URL mappingFile) throws Exception {
        super(xmlFile, sessionVariablesOnly, mappingFile);
    }

    public XmlClaimReader(final String xml, final XPathMappingList valueMapping, final Boolean sessionVariablesOnly) throws InstantiationException {
        super(xml, valueMapping, sessionVariablesOnly);
        NOT_SUPPORTED = new HashSet<>();
        IGNORE_MAPPING = new HashSet<>(Arrays.asList(new String[]{
                "DWPBody/DWPCATransaction/DWPCAClaim/EvidenceList/Evidence/Title"       // TODO
        }));
    }

    @Override
    protected void createCollectionDetails() {
        collections.add(new CollectionDetails("JobDetails", "DWPBody/DWPCATransaction/DWPCAClaim/Incomes/Employment/JobDetails/", "DWPBody/DWPCATransaction/DWPCAClaim/Incomes/Employment/JobDetails/Employer/CurrentlyEmployed/QuestionLabel"));
        collections.add(new CollectionDetails("breakshospital", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel"));
        collections.add(new CollectionDetails("breaksrespite", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel"));
        collections.add(new CollectionDetails("breaksother", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel"));
    }

    @Override
    protected boolean createCollection(final Map<String, Object> values, final Node node, final String parentPath, final String data) {
        try {
            CollectionDetails collectionDetails = findParentPath(parentPath, node);
            if (collectionDetails != null) {
                Map<String, Object> latestValues;
                List<Map<String, Object>> collection = (List<Map<String, Object>>) values.get(collectionDetails.getCollectionName());
                if (collectionDetails.getNewMapKey().equals(parentPath)) {
                    latestValues = new HashMap<>();
                    if (collection == null) {
                        collection = new ArrayList<>();
                        values.put(collectionDetails.getCollectionName(), collection);
                    }
                    latestValues.put(collectionDetails.getCollectionName() + "_id", String.valueOf(collection.size()+1));
                    collection.add(latestValues);
                } else {
                    latestValues = collection.get(collection.size()-1);
                }
                //find mapping
                if (collectionDetails.getCollectionName().contains("breaks")) {
                    addBreaks(collectionDetails, latestValues, parentPath, data);
                } else if (collectionDetails.getCollectionName().contains("JobDetails")) {
                    addJobDetails(collectionDetails, latestValues, parentPath, data);
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            LOG.error("Unknown failure. parentPath:{}", parentPath, e);
            return false;
        }
        return true;
    }

    private void addJobDetails(final CollectionDetails collectionDetails, final Map<String, Object> latestValues, final String parentPath, final String data) {
        final String path = parentPath.replace(collectionDetails.getStartWith(), "");
        XPathMapping xPathMapping = findMapping(path, collectionDetails.getCollectionName());
        if (xPathMapping == null) {
            createAdditionalMappings(mappingFileName + "." + collectionDetails.getCollectionName().toLowerCase(), collectionDetails.getCollectionName());
            xPathMapping = findMapping(path, collectionDetails.getCollectionName());
        }
        processMapping(xPathMapping, latestValues, data);
    }

    private void addBreaks(final CollectionDetails collectionDetails, final Map<String, Object> latestValues, String parentPath, String data) {
        //need to decipher which type of break it is BreaksType = DPHospital/YouHospital,DPRespite/YouRespite or Other
        final String path = parentPath.replace(collectionDetails.getStartWith(), "");
        XPathMapping xPathMapping = findMapping(path, fileSuffix);
        if (xPathMapping == null) {
            createAdditionalMappings(mappingFileName + ".breaks" + fileSuffix, fileSuffix);
            xPathMapping = findMapping(path, fileSuffix);
        }
        processMapping(xPathMapping, latestValues, data.replace("@dpname", "Caree").replace("You", "Carer"));
    }

    private String getAnswerToBreakQuestion(final Node node) {
        try {
            return node.getParentNode().getNextSibling().getFirstChild().getNodeValue();
        } catch (Exception e) {
            LOG.error("Exception in getting answer ", e);
        }
        return null;
    }

    private CollectionDetails findParentPath(final String parentPath, final Node node) {
        if (parentPath.contains("DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak")) {
            if (parentPath.equals("DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel")) {
                fileSuffix = getAnswerToBreakQuestion(node).toLowerCase();
            }
            return findCollectionForSuffix("breaks" + fileSuffix.replace("dp", "").replace("you", ""));
        }

        for (CollectionDetails collectionDetails : collections) {
            if (parentPath.equals(collectionDetails.getNewMapKey()) || parentPath.startsWith(collectionDetails.getStartWith())) {
                return collectionDetails;
            }
        }
        return null;
    }

    private CollectionDetails findCollectionForSuffix(String collectionName) {
        for (CollectionDetails collectionDetails : collections) {
            if (collectionDetails.getCollectionName().equals(collectionName)) {
                return collectionDetails;
            }
        }
        return null;
    }
}