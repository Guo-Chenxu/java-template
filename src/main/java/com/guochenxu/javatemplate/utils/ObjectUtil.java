package com.guochenxu.javatemplate.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目工具
 *
 * @author: guoch
 * @create: 2025-01-06 16:47
 * @version: 1.0
 */

@Slf4j
public class ObjectUtil {

    /**
     * 将旧实体类和新的合并，以新的为准
     */
    @SneakyThrows
    public static <T> T mergeObjects(T source, T target) {
        // Ensure both parameters are non-null and of the same type.
        if (source == null || target == null || !source.getClass().equals(target.getClass())) {
            throw new IllegalArgumentException("Source and target must be non-null and of the same type.");
        }

        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object newValue = field.get(target);
            Object oldValue = field.get(source);

            if (newValue instanceof String && StringUtils.isBlank((String) newValue)) {
                field.set(target, oldValue != null ? oldValue : newValue);
            } else if ((newValue instanceof Collection && CollectionUtils.isEmpty((Collection<?>) newValue))) {
                field.set(target, oldValue != null ? oldValue : newValue);
            } else if (newValue == null) {
                field.set(target, oldValue != null ? oldValue : newValue);
            }
            field.setAccessible(false);
        }
        return target;
    }

    private static Object getDefaultValue(Class<?> type) {
        if (type == int.class || type == long.class) {
            return 0;
        } else if (type == float.class || type == double.class) {
            return 0.0;
        } else if (type == boolean.class) {
            return false;
        } else if (type == char.class) {
            return '\u0000';
        } else if (type == byte.class) {
            return (byte) 0;
        } else if (type == short.class) {
            return (short) 0;
        } else {
            return null;
        }
    }


    /**
     * 获取指定接口中所有的静态字符串常量
     */
    public static Map<String, String> getConstants(Class<?> clazz) {
        Map<String, String> constantMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (java.lang.reflect.Modifier.isStatic(modifiers)
                    && java.lang.reflect.Modifier.isFinal(modifiers)) {
                try {
                    Object value = field.get(null);
                    constantMap.put(field.getName(), value != null ? value.toString() : "null");
                } catch (IllegalAccessException e) {
                    log.error("获取静态常量失败: " + e);
                }
            }
        }
        return constantMap;
    }
}
