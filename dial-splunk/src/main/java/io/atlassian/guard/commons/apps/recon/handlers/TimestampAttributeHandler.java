package io.atlassian.guard.commons.apps.recon.handlers;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static io.atlassian.guard.commons.utils.HandyUtils.isEmpty;
import static java.time.Instant.ofEpochMilli;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class TimestampAttributeHandler implements AttributeHandler {
    private static String readableDifference(String isoTimestamp1, String isoTimestamp2) {
        if (isEmpty(isoTimestamp1) || isEmpty(isoTimestamp2)) {
            return "";
        }
        Instant inst1 = Instant.parse(isoTimestamp1);
        Instant inst2 = Instant.parse(isoTimestamp2);
        Duration duration = inst1.compareTo(inst2) < 0 ? Duration.between(inst1, inst2) : Duration.between(inst2, inst1);
        return DurationFormatUtils.formatDurationWords(duration.toMillis(), true, true);
    }

    @Override
    public Object rectify(Object raw) {
        if (isEmpty(raw) || "null".equals(raw)) {
            return null;
        } else if (raw instanceof String str) {
            return str;
        } else if (raw instanceof Number num) {
            if (num.longValue() == 0) {
                return null;
            }
            return ISO_INSTANT.format(ofEpochMilli(num.longValue()));
        }
        throw new RuntimeException("Cannot convert to ISO date from " + raw.getClass().getName());
    }

    @Override
    public String describeDifference(Collection<Map.Entry<String, Object>> differences) {
        if (differences.size() < 2) {
            return "";
        }
        var differencesList = new ArrayList<>(differences);
        var val1 = (String) differencesList.get(0).getValue();
        var val2 = (String) differencesList.get(1).getValue();
        return readableDifference(val1, val2);
    }

}
