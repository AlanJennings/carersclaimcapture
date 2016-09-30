package uk.gov.dwp.carersallowance.utils;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public final class CollectionUtils {
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
}
