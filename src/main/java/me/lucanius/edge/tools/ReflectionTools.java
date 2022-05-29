package me.lucanius.edge.tools;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@UtilityClass
public class ReflectionTools {

    @SneakyThrows
    public Field setAccessibleAndGet(Class<?> clazz, String fieldName) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field;
    }
}
