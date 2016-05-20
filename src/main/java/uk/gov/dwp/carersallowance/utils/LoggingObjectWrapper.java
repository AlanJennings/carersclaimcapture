package uk.gov.dwp.carersallowance.utils;

import java.util.Arrays;

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
            return Arrays.asList(object).toString();
        }

        return object.toString();
    }
}