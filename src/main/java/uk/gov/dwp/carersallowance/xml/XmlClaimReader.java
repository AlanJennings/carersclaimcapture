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

public class XmlClaimReader extends XmlReader {
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
        collectionDetails.add(new CollectionDetail("JobDetails", "DWPBody/DWPCATransaction/DWPCAClaim/Incomes/Employment/JobDetails/", "DWPBody/DWPCATransaction/DWPCAClaim/Incomes/Employment/JobDetails/Employer/CurrentlyEmployed/QuestionLabel"));
        collectionDetails.add(new CollectionDetail("breakshospital", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel"));
        collectionDetails.add(new CollectionDetail("breaksrespite", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel"));
        collectionDetails.add(new CollectionDetail("breaksother", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/", "DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel"));
    }

    @Override
    protected boolean createCollection(final Map<String, Object> values, final Node node, final String parentPath, final String data) {
        try {
            CollectionDetail collectionDetail = findParentPath(parentPath, node);
            if (collectionDetail != null) {
                Map<String, Object> latestValues;
                List<Map<String, Object>> collection = (List<Map<String, Object>>) values.get(collectionDetail.getCollectionName());
                if (collectionDetail.getNewMapKey().equals(parentPath)) {
                    latestValues = new HashMap<>();
                    if (collection == null) {
                        collection = new ArrayList<>();
                        values.put(collectionDetail.getCollectionName(), collection);
                    }
                    latestValues.put(collectionDetail.getCollectionName() + "_id", String.valueOf(collection.size()+1));
                    collection.add(latestValues);
                } else {
                    latestValues = collection.get(collection.size()-1);
                }
                //find mapping
                if (collectionDetail.getCollectionName().contains("breaks")) {
                    addBreaks(collectionDetail, latestValues, parentPath, data);
                } else if (collectionDetail.getCollectionName().contains("JobDetails")) {
                    addJobDetails(collectionDetail, latestValues, parentPath, data);
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

    private void addJobDetails(final CollectionDetail collectionDetail, final Map<String, Object> latestValues, final String parentPath, final String data) {
        final String path = parentPath.replace(collectionDetail.getStartWith(), "");
        XPathMapping xPathMapping = findMapping(path, collectionDetail.getCollectionName());
        if (xPathMapping == null) {
            createAdditionalMappings(mappingFileName + "." + collectionDetail.getCollectionName().toLowerCase(), collectionDetail.getCollectionName());
            xPathMapping = findMapping(path, collectionDetail.getCollectionName());
        }
        processMapping(xPathMapping, latestValues, data);
    }

    private void addBreaks(final CollectionDetail collectionDetail, final Map<String, Object> latestValues, String parentPath, String data) {
        //need to decipher which type of break it is BreaksType = DPHospital/YouHospital,DPRespite/YouRespite or Other
        final String path = parentPath.replace(collectionDetail.getStartWith(), "");
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

    private CollectionDetail findParentPath(final String parentPath, final Node node) {
        if (parentPath.contains("DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak")) {
            if (parentPath.equals("DWPBody/DWPCATransaction/DWPCAClaim/Caree/CareBreak/BreaksType/QuestionLabel")) {
                fileSuffix = getAnswerToBreakQuestion(node).toLowerCase();
            }
            return findCollectionForSuffix("breaks" + fileSuffix.replace("dp", "").replace("you", ""));
        }

        for (CollectionDetail collectionDetail : collectionDetails) {
            if (parentPath.equals(collectionDetail.getNewMapKey()) || parentPath.startsWith(collectionDetail.getStartWith())) {
                return collectionDetail;
            }
        }
        return null;
    }

    private CollectionDetail findCollectionForSuffix(String collectionName) {
        for (CollectionDetail collectionDetail : collectionDetails) {
            if (collectionDetail.getCollectionName().equals(collectionName)) {
                return collectionDetail;
            }
        }
        return null;
    }
}