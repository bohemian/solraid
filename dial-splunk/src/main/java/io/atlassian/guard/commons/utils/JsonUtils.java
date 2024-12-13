package io.atlassian.guard.commons.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.experimental.UtilityClass;

import static java.time.Instant.ofEpochMilli;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@UtilityClass
public class JsonUtils {

    public <T> T get(String jsonString, String jsonPath) {
        try {
            return JsonPath.parse(jsonString).read(jsonPath);
        } catch (PathNotFoundException ignore) {
            return null;
        }
    }

}
