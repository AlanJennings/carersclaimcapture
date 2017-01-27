package uk.gov.dwp.carersallowance.controller.subform;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.session.FieldCollection;
import uk.gov.dwp.carersallowance.session.UnknownRecordException;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.utils.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by peterwhitehead on 27/01/2017.
 */
public class SubFormProcessing {
    private static final Logger LOG = LoggerFactory.getLogger(SubFormProcessing.class);

    public String deleteFieldCollectionRecord(final Session session, final String idToDelete, final HttpServletRequest request, final String fieldCollectionName, final String idField) {
        LOG.trace("Starting SubFormProcessing.deleteEmployment");
        try {
            Parameters.validateMandatoryArgs(new Object[]{idToDelete, request, session}, new String[]{ "idToDelete", "request", "session" });

            Integer foundIndex = null;
            final List<Map<String, String>> fieldCollectionList = getFieldCollections(session, fieldCollectionName, false);
            for (int index = 0; index < fieldCollectionList.size(); index++) {
                final Map<String, String> map = fieldCollectionList.get(index);
                if (idToDelete.equals(map.get(idField))) {
                    foundIndex = Integer.valueOf(index);
                    break;
                }
            }

            if (foundIndex != null) {
                fieldCollectionList.remove(foundIndex.intValue());
            } else {
                throw new UnknownRecordException("Unknown record id: " + idToDelete);
            }

            return "redirect:" + PropertyUtils.getCurrentPage(request);
        } finally {
            LOG.trace("Ending SubFormProcessing.deleteEmployment");
        }
    }

    public String editFieldCollectionRecord(final Session session, final String idToChange, final String fieldCollectionName, final String idField, final String editingPage) {
        Parameters.validateMandatoryArgs(new Object[]{idToChange, session}, new String[]{"idToChange", "session"});

        // copy the record values into the edit fields in the session
        final List<Map<String, String>> records = getFieldCollections(session, fieldCollectionName, true);
        final Map<String, String> record = FieldCollection.getFieldCollection(records, idField, idToChange);
        if (record == null) {
            throw new UnknownRecordException("Unknown record id: " + idToChange);
        } else {
            String[] fields = record.keySet().toArray(new String[]{});
            copyMapToSession(record, fields, session);
        }
        return "redirect:" + editingPage;
    }

    public String getNextIdValue(final List<Map<String, String>> fieldCollectionList, final String idFieldName) {
        int value = 0;
        for (int index = 0; index < fieldCollectionList.size(); index++) {
            final Map<String, String> existingFieldCollection = fieldCollectionList.get(index);
            final String id = existingFieldCollection.get(idFieldName);
            if (id == null) {
                LOG.error("Missing ID field: " + idFieldName + " for record(" + index + "): " + existingFieldCollection);
                throw new IllegalArgumentException("Missing ID field: " + idFieldName + " for record(" + index + ")");
            }
            try {
                final int idInt = Integer.parseInt(id.trim());
                if (idInt > value) {
                    value = idInt;
                }
            } catch (NumberFormatException e) {
                LOG.error("ID field: " + idFieldName + " value is not an integer(" + id + ")");
                throw new IllegalArgumentException("ID field: " + idFieldName + " value is not an integer(" + id + ")", e);
            }
        }
        final String result = Integer.toString(value + 1);
        return result;
    }

    /**
     * Populate a fieldCollection entry from session field values as identified by the "fields"
     * parameter with the specific entry being identified by the "idField" value.
     *
     * If the value corresponding to idField is blank then its a new record and it is added to
     * the fieldCollection list, otherwise it is editing an existing record in the fieldCollection
     * list.  The existing record is completely cleared before being overwritten by the new data.
     *
     * If the matching id cannot be found an IllegalArgumentException is thrown (TODO replace
     * this with a proper exception).
     *
     * @throws IllegalArgumentException if the record being edited cannot be found.
     */
    public void populateFieldCollectionEntry(final Session session, final String fieldCollectionName, final String[] fields, final String idField) {
        LOG.trace("Starting AbstractFormController.populateFieldCollectionEntry");
        try {
            Parameters.validateMandatoryArgs(session,  "session");

            if (StringUtils.isEmpty(fieldCollectionName)) {
                LOG.debug("fieldCollectionName empty, nothing to do");
                return;
            }

            // get the fieldCollection list from the session (create if needed)
            final List<Map<String, String>> fieldCollection = getFieldCollections(session, fieldCollectionName, true);
            LOG.debug("fieldCollection before = {}", fieldCollection);

            // get ALL field values from the session
            final Map<String, String[]> attributes = getSessionStringAttributes(session);
            // and filter them to only these keys we are interested in
            final Map<String, String> record = FieldCollection.getFieldValues(attributes, fields);
            LOG.debug("record = {}", record);

            // identify the record id field, and retrieve its value (if it exists)
            final String recordId = record.get(idField);

            // if there is no current recordId, then we are editing a new record, so create a new record id
            // and update the record, then add the new record to the fieldCollection list.
            if (StringUtils.isEmpty(recordId)) {
                LOG.debug("Creating new record");
                final String newRecordId = getNextIdValue(fieldCollection, idField);

                record.put(idField, newRecordId);
                fieldCollection.add(record);

                // if there is a current recordId, then we are editing an existing record, iterate over the
                // existing records and match the recordId. On match, update the existing record, IF the record
                // ids cannot be matched log and error and throw an exception
            } else {
                Boolean existingRecordFound = false;
                if (StringUtils.isEmpty(recordId) == false) {
                    for (final Map<String, String> existingRecord: fieldCollection) {
                        final String existingRecordId = existingRecord.get(idField);
                        if (recordId.equals(existingRecordId)) {
                            // copy over the existing record (this assumes a complete record in hand)
                            existingRecord.clear();
                            existingRecord.putAll(record);
                            existingRecordFound = true;
                            break;
                        }
                    }
                }

                if (existingRecordFound == false) {
                    LOG.error("Unknown record ID: " + recordId);
                    throw new IllegalArgumentException("Unknown record ID: " + recordId);
                }
            }
            LOG.debug("fieldCollection after = {}", fieldCollection);
            LOG.debug("getFieldCollections('<fieldCollectionName>') = {}", getFieldCollections(session, fieldCollectionName, false));

            // clean up the session by removing the individual field values for the item create/edit screens
            removeFromSession(session, fields);
        } finally {
            LOG.trace("Ending AbstractFormController.populateFieldCollectionEntry");
        }
    }

    private void removeFromSession(final Session session, final String[] fieldNames) {
        LOG.trace("Started AbstractFormController.removeFromSession");
        try {
            Parameters.validateMandatoryArgs(session, "session");
            if (fieldNames == null) {
                return;
            }

            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            for (final String fieldName: fieldNames) {
                session.removeAttribute(fieldName);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.removeFromSession");
        }
    }

    private void copyMapToSession(Map<String, String> map, String[] fieldNames, Session session) {
        LOG.trace("Started AbstractFormController.copyMapToSession");
        try {
            Parameters.validateMandatoryArgs(new Object[]{map, session}, new String[]{"map", "session"});
            if(fieldNames == null) {
                return;
            }

            for(String fieldName: fieldNames) {
                String fieldValue = map.get(fieldName);
                LOG.info("Adding session fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                session.setAttribute(fieldName, fieldValue);
            }
        } finally {
            LOG.trace("Ending AbstractFormController.copyMapToSession");
        }
    }

    /**
     * make the map the field collection
     * make the list the field collection list? (yuk)
     */
    @SuppressWarnings({ "unchecked" })
    protected List<Map<String, String>> getFieldCollections(final Session session, final String collectionName, final Boolean create) {
        LOG.trace("Started AbstractFormController.getFieldCollections");
        try {
            Parameters.validateMandatoryArgs(session, "session");
            LOG.debug("collectionName = {}, create = {}", collectionName, create);

            if (collectionName == null) {
                return null;
            }

            Object value = session.getAttribute(collectionName);
            if (value != null) {
                LOG.debug("found collection({}) = {}", collectionName, value);
            } else {
                LOG.debug("No collection found");
                if (create) {
                    LOG.debug("Creating new Field Collection");
                    final List<Map<String, String>>newFieldCollection = new ArrayList<>();
                    session.setAttribute(collectionName,  newFieldCollection);
                    return newFieldCollection;
                } else {
                    LOG.debug("returning null");
                    return null;
                }
            }

            if (value instanceof List) {
                return (List<Map<String, String>>)value;
            }

            throw new IllegalStateException("FieldCollection List not of expected type:" + value.getClass().getName() + ", expecting " + List.class.getName());
        } finally {
            LOG.trace("Ending AbstractFormController.getFieldCollections");
        }
    }

    /**
     * Get the session attributes as if they were a request parameter map, i.e.
     * return those that have String or String[] values along with their keys in a map.
     * @return return a map of values or null if session is null
     */
    public static Map<String, String[]> getSessionStringAttributes(final Session session) {
        if (session == null) {
            return null;
        }

        final Map<String, String[]> map = new HashMap<>();
        final List<String> attrNames = session.getAttributeNames();
        for (final String attrName: attrNames) {
            final Object object = session.getAttribute(attrName);
            if(object == null || object instanceof String[]) {
                map.put(attrName, (String[])object);
            } else if(object instanceof String) {
                map.put(attrName, new String[]{(String)object});
            } else {
                // ignore it
            }
        }
        return map;
    }
}
