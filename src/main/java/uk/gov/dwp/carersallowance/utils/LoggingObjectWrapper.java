package uk.gov.dwp.carersallowance.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Wrap an object so that when it is logged we get readable output, but don't incur the cost of
 * making the message until we do. (at the cost of creating a new instance of LoggingObjectWrapper)
 */
public class LoggingObjectWrapper {
    private Object object;

    public LoggingObjectWrapper(Object object) {
        this.object = object;
    }

    public String toString() {
        if(object == null) {
            return null;
        }

        if(object instanceof String[]) {
            List<String> list = Arrays.asList((String[])object);
            return list.toString();
        }

        return object.toString();
    }

    public static void main(String[] args) {
        Object[] objects = new Object[]{"single", new String[]{"one", "two"}, null};
        for(Object object : objects) {
            System.out.println(object);
            System.out.println(new LoggingObjectWrapper(object));
            System.out.println();
        }
    }
}