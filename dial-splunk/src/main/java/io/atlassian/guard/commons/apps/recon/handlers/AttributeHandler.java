package io.atlassian.guard.commons.apps.recon.handlers;

import java.util.Collection;
import java.util.Map;

public interface AttributeHandler {
    default Object rectify(Object raw) {
        return raw;
    }

    default String describeDifference(Collection<Map.Entry<String, Object>> differences) {
        return "";
    }
}
