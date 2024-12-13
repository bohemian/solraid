package io.atlassian.guard.commons.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

public class HandyUtils {

    @SneakyThrows
    public static String fileToString(String filename) {
        return Files.readString(Paths.get(filename));
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String s) {
            return s.isEmpty() || s.equals("null");
        } else if (obj instanceof Collection<?> c) {
            return c.isEmpty();
        } else if (obj instanceof Map<?, ?> m) {
            return m.isEmpty();
        } else if (obj instanceof Number n) {
            return n.doubleValue() == 0.0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

}
