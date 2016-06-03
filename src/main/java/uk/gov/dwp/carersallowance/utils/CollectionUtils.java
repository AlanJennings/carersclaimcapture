package uk.gov.dwp.carersallowance.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class CollectionUtils {
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
                    item = item.toUpperCase();
                }
                newCollection.add(item);
            }

            return newCollection;
        } catch(RuntimeException | InstantiationException | IllegalAccessException  e) {
            throw new RuntimeException("Unable to create new collection", e);
        }
    }

    public static void main(String[] args) {
        String[] values = {"one", "two", "Three", "FOUR"};
        Set<String> set = new TreeSet<>(Arrays.asList(values));
        Set<String> newSet = toUpperCase(set);

        System.out.println("set: " + set.getClass() + " = " + set);
        System.out.println("newSet: " + newSet.getClass() + " = " + newSet);
    }
}
