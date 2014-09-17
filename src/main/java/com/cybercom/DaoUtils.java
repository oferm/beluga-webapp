package com.cybercom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 2:12 PM
 */
public class DaoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoUtils.class);

    /**
     * Expands the object array by 1 and adds a new object at the end
     *
     * @param objects   The original object array
     * @param newObject The new object to add
     * @return The new object array
     */
    public Object[] plusOne(final Object[] objects, final Object newObject) {
        Object[] result = new Object[objects.length + 1];
        int i = 0;
        for (i = 0; i < objects.length; i++) {
            result[i] = objects[i];
        }
        result[i] = newObject;
        return result;
    }
}
