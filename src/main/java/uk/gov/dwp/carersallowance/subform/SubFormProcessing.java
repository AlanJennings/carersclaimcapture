package uk.gov.dwp.carersallowance.subform;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;
import uk.gov.dwp.carersallowance.session.FieldCollection;
import uk.gov.dwp.carersallowance.session.UnknownRecordException;
import uk.gov.dwp.carersallowance.sessiondata.Session;
import uk.gov.dwp.carersallowance.utils.Parameters;
import uk.gov.dwp.carersallowance.validations.Dependency;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
  * Created by peterwhitehead on 27/01/2017.
  */
public class SubFormProcessing {
    private static final Logger LOG = LoggerFactory.getLogger(SubFormProcessing.class);

    private static final String FIELD_COLLECTION_NAME           = "subform.%s.field.collection.name";
    private static final String FIELD_COLLECTION_RECORD_ID      = "subform.%s.field.collection.record.id";
    private static final String FIRST_PAGE_ON_EMPTY             = "subform.%s.first.page.on.empty.collection";
    private static final String LAST_PAGE_CLEAR_ATTRIBUTES      = "subform.%s.last.page.clear.attributes";
    private static final String COLLECTION_EMPTY_ON_DELETE_PAGE = "subform.%s.collection.empty.on.delete.page";
    private static final String ON_EDIT_SET_ATTRIBUTE           = "subform.%s.on.edit.set.attributes";
    private static final String LAST_TO_FIRST_PAGE_DEPENDENCY   = "subform.%s.last.page.first.page.dependency";
    private static final String IGNORE_FIELDS                   = "subform.%s.ignore.fields";

    private final String subFormName;
    private final String fieldCollectionName;
    private final String fieldCollectionRecordId;
    private final String firstPageOnEmpty;
    private final String lastToFirstPage;
    private final String ignoreFields;
    private final List<String> lastPageClearAttributes;
    private final String emptyOnDeletePage;
    private final List<Dependency> onEditSetAttributes;
    private final MessageSource messageSource;

    public SubFormProcessing(final String subFormName, final MessageSource messageSource) {
        this.subFormName = subFormName;
        this.messageSource = messageSource;
        this.fieldCollectionName = getMessage(String.format(FIELD_COLLECTION_NAME, subFormName));
        this.fieldCollectionRecordId = getMessage(String.format(FIELD_COLLECTION_RECORD_ID, subFormName));
        this.firstPageOnEmpty = getMessage(String.format(FIRST_PAGE_ON_EMPTY, subFormName));
        this.emptyOnDeletePage = getMessage(String.format(COLLECTION_EMPTY_ON_DELETE_PAGE, subFormName));
        this.lastToFirstPage = getMessage(String.format(LAST_TO_FIRST_PAGE_DEPENDENCY, subFormName));
        this.ignoreFields = getMessage(String.format(IGNORE_FIELDS, subFormName));
        this.lastPageClearAttributes = getLastPageAttributes(getMessage(String.format(LAST_PAGE_CLEAR_ATTRIBUTES, subFormName)));
        this.onEditSetAttributes = getOnEditAttributes(getMessage(String.format(ON_EDIT_SET_ATTRIBUTE, subFormName)));
    }

    private List<Dependency> getOnEditAttributes(final String message) {
        final List<Dependency> dependencies = new ArrayList<>();
        if (message == null) {
            return dependencies;
        }
        final String[] attributes = message.split(",");
        for (final String attribute : attributes) {
            try {
                dependencies.add(Dependency.parseSingleLine(attribute));
            } catch (ParseException pe) {
                LOG.error("unable to parse edit attribute:{}", attribute, pe);
            }
        }
        return dependencies;
    }

    private List<String> getLastPageAttributes(final String message) {
        if (message == null) {
            return new ArrayList<>();
        }
        return Stream.of(message.split(",")).collect(Collectors.toList());
    }

    private String getMessage(final String code) {
        return messageSource.getMessage(code, null, null, Locale.getDefault());
    }

    private String deleteFieldCollectionRecord(final Session session, final String idToDelete, final String currentPage, final String fieldCollectionName, final String idField) {
        LOG.trace("Starting SubFormProcessing.deleteEmployment");
        try {
            Parameters.validateMandatoryArgs(new Object[]{idToDelete, currentPage, session}, new String[]{ "idToDelete", "currentPage", "session" });
    
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
    
            return "redirect:" + currentPage;
        } finally {
            LOG.trace("Ending SubFormProcessing.deleteEmployment");
        }
    }
    
    private String editFieldCollectionRecord(final Session session, final String idToChange, final String fieldCollectionName, final String idField, final String editingPage) {
        LOG.trace("Starting SubFormProcessing.editFieldCollectionRecord");
        try {
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
        } finally {
            LOG.trace("Ending SubFormProcessing.editFieldCollectionRecord");
        }
    }
    
    private String getNextIdValue(final List<Map<String, String>> fieldCollectionList, final String idFieldName) {
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
    private void populateFieldCollectionEntry(final Session session, final String fieldCollectionName, final String[] fields, final String idField) {
        LOG.trace("Starting SubFormProcessing.populateFieldCollectionEntry");
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
            removeFromSession(session, fields, fieldCollectionName);
        } finally {
            LOG.trace("Ending SubFormProcessing.populateFieldCollectionEntry");
        }
    }
    
    private void removeFromSession(final Session session, final String[] fieldNames, String fieldCollectionName) {
        LOG.trace("Started SubFormProcessing.removeFromSession");
        try {
            Parameters.validateMandatoryArgs(session, "session");
            if (fieldNames == null) {
                return;
            }
    
            LOG.debug("fieldNames = {}", Arrays.asList(fieldNames));
            for (final String fieldName: fieldNames) {
                if (ignoreFields == null || ignoreFields.contains(fieldCollectionName + "." + fieldName) == false) {
                    session.removeAttribute(fieldName);
                }
            }
        } finally {
            LOG.trace("Ending SubFormProcessing.removeFromSession");
        }
    }
    
    private void copyMapToSession(Map<String, String> map, String[] fieldNames, Session session) {
        LOG.trace("Started SubFormProcessing.copyMapToSession");
        try {
            Parameters.validateMandatoryArgs(new Object[]{map, session}, new String[]{"map", "session"});
            if (fieldNames == null) {
                return;
            }
    
            for (String fieldName: fieldNames) {
                String fieldValue = map.get(fieldName);
                LOG.info("Adding session fieldName = {}, fieldValue = {}", fieldName, fieldValue);
                session.setAttribute(fieldName, fieldValue);
            }
        } finally {
            LOG.trace("Ending SubFormProcessing.copyMapToSession");
        }
    }
    
    /**
     * make the map the field collection
     * make the list the field collection list? (yuk)
     */
    @SuppressWarnings({ "unchecked" })
    private List<Map<String, String>> getFieldCollections(final Session session, final String collectionName, final Boolean create) {
        LOG.trace("Started SubFormProcessing.getFieldCollections");
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
            LOG.trace("Ending SubFormProcessing.getFieldCollections");
        }
    }
    
    /**
     * Get the session attributes as if they were a request parameter map, i.e.
     * return those that have String or String[] values along with their keys in a map.
     * @return return a map of values or null if session is null
     */
    private Map<String, String[]> getSessionStringAttributes(final Session session) {
        if (session == null) {
            return null;
        }

        final Map<String, String[]> map = new HashMap<>();
        final List<String> attrNames = session.getAttributeNames();
        for (final String attrName: attrNames) {
            final Object object = session.getAttribute(attrName);
            if (object == null || object instanceof String[]) {
                map.put(attrName, (String[])object);
            } else if (object instanceof String) {
                map.put(attrName, new String[]{(String)object});
            } else {
                // ignore it
            }
        }
        return map;
    }

    private void setSessionAttributesOnEdit(final Session session) {
        for (final Dependency dependency : onEditSetAttributes) {
            session.setAttribute(dependency.getDependantField(), dependency.getFieldValue());
        }
    }

    public String processEditRowInCollection(final String idToChange, final Session session) throws UnknownRecordException {
        LOG.trace("Started SubFormProcessing.processEditRowInCollection");
        try {
            final String editPage = editFieldCollectionRecord(session, idToChange, fieldCollectionName, fieldCollectionRecordId, firstPageOnEmpty);
            for (final Dependency dependency : onEditSetAttributes) {
                session.setAttribute(dependency.getDependantField(), dependency.getFieldValue());
            }
            return editPage;
        } finally {
            LOG.trace("Ending SubFormProcessing.processEditRowInCollection");
        }
    }

    public String processDeleteRowInCollection(final String idToDelete, final String currentPage, final Session session) throws UnknownRecordException {
        LOG.trace("Started SubFormProcessing.processDeleteRowInCollection");
        try {
            final String newPage = deleteFieldCollectionRecord(session, idToDelete, currentPage, fieldCollectionName, fieldCollectionRecordId);
            final List<Map<String, String>> collection = getFieldCollections(session, fieldCollectionName, false);
            if (collection == null || collection.isEmpty()) {
                return "redirect:" + emptyOnDeletePage;
            }
            return newPage;
        } finally {
            LOG.trace("Ending SubFormProcessing.processDeleteRowInCollection");
        }
    }

    public String processLastPageInCollection(final Session session, final MessageSource messageSource, final List<String> pageList) {
        LOG.trace("Started SubFormProcessing.processLastPageInCollection");
        try {
            final List<String[]> fields = pageList.stream().map(field -> FieldCollection.getFields(messageSource, field)).collect(Collectors.toList());
            fields.add(new String[]{ fieldCollectionRecordId });
            final String[] fieldCollectionFields = convertToStringArray(fields);
            populateFieldCollectionEntry(session, fieldCollectionName, fieldCollectionFields, fieldCollectionRecordId);
            for (final String attribute : lastPageClearAttributes) {
                session.removeAttribute(attribute);
            }
            //return first page again
            if (Dependency.pageDependencyFulfilled(session.getData(), lastToFirstPage, Boolean.TRUE)) {
                return pageList.get(0);
            }
            return null;
        } finally {
            LOG.trace("Ending SubFormProcessing.processLastPageInCollection");
        }
    }

    private String[] convertToStringArray(List<String[]> fieldArrays) {
        Set<String> fieldSet = new HashSet<>();
        for (String[] fields: fieldArrays) {
            for(String field: fields) {
                fieldSet.add(field);
            }
        }
        return fieldSet.toArray(new String[]{});
    }

    public String processFirstPageInCollection(final String firstPage, final Session session) {
        LOG.trace("Started SubFormProcessing.processFirstPageInCollection");
        try {
            final List<Map<String, String>> collection = getFieldCollections(session, fieldCollectionName, false);
            if (CollectionUtils.isEmpty(collection)) {
                setSessionAttributesOnEdit(session);
                return firstPageOnEmpty;
            }
            return firstPage;
        } finally {
            LOG.trace("Ending SubFormProcessing.processFirstPageInCollection");
        }
    }

    public String previousOnEmpty(final String currentPage, final String previousPage, final Session session) {
        final List<Map<String, String>> collection = getFieldCollections(session, fieldCollectionName, false);
        if (currentPage.equals(firstPageOnEmpty) && CollectionUtils.isEmpty(collection)) {
            return emptyOnDeletePage;
        }
        return previousPage;
    }

    public String getFieldCollectionName() {
        return fieldCollectionName;
    }
}