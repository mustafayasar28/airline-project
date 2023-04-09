package com.technarts.airline.Airline.utils;

import java.lang.reflect.Field;

public class UpdateUtils {

    public static <T> void updateNonNullFields(T source, T target) throws IllegalAccessException {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        if (!sourceClass.equals(targetClass)) {
            throw new IllegalArgumentException("Source and target objects must be of the same class");
        }

        Field[] fields = sourceClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object sourceValue = field.get(source);
            if (sourceValue != null) {
                if (sourceValue instanceof Integer) {
                    Integer sourceInt = (Integer) sourceValue;
                    if (sourceInt != null) {
                        field.set(target, sourceInt);
                    }
                } else {
                    field.set(target, sourceValue);
                }
            }
        }
    }
}