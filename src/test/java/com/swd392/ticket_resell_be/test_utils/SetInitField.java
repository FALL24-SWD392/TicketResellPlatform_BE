package com.swd392.ticket_resell_be.test_utils;

import java.lang.reflect.Field;

public class SetInitField {
    public static void setField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
