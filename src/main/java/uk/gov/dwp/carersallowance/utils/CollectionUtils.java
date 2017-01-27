package uk.gov.dwp.carersallowance.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.carersallowance.sessiondata.Session;

import java.util.*;

public final class CollectionUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CollectionUtils.class);

    private CollectionUtils() {}

    public static Set<String> toUpperCase(Set<String> collection) {
        return (Set<String>)toUpperCase((Collection<String>)collection);
    }

    public static Collection<String> toUpperCase(Collection<String> collection) {
        if(collection == null) {
            return null;
        }

        try {
            @SuppressWarnings("unchecked")
            Collection<String> newCollection = collection.getClass().newInstance();
            for(String item: collection) {
                if(item != null) {
                    item = item.toUpperCase(Locale.getDefault());
                }
                newCollection.add(item);
            }

            return newCollection;
        } catch(RuntimeException | InstantiationException | IllegalAccessException  e) {
            throw new RuntimeException("Unable to create new collection", e);
        }
    }

    public static Map<String, String[]> getAllFieldValues(Session session) {
        LOG.trace("Started CollectionUtils.getAllFieldValues");
        try {
            if(session == null) {
                return null;
            }

            StringBuffer buffer = new StringBuffer();

            Map<String, String[]> map = new HashMap<>();
            List<String> sessionNames = session.getAttributeNames();
            Collections.sort(sessionNames);
            for(String attrName: sessionNames) {
                Object attrValue = session.getAttribute(attrName);
                if(attrValue instanceof String) {
                    buffer.append(attrName).append(" = ").append((String)attrValue).append(", ");
                    String[] attrValues = new String[]{(String)attrValue};
                    map.put(attrName, attrValues);
                } else if(attrValue instanceof String[]) {
                    buffer.append(attrName).append(" = ").append(Arrays.asList((String[])attrValue)).append(", ");
                    map.put(attrName, (String[])attrValue);
                } else {
                    // do nothing
                }
            }

            LOG.debug("all field values = {}", buffer.toString());
            return map;
        } finally {
            LOG.trace("Ending CollectionUtils.getAllFieldValues");
        }
    }
}
