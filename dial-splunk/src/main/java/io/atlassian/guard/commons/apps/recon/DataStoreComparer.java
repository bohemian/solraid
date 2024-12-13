package io.atlassian.guard.commons.apps.recon;

import io.atlassian.guard.commons.apps.recon.stores.DataStore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Data
public class DataStoreComparer {
    private static final String MISSING_VALUE = "null";
    private static final Function<String, Map<Attribute, Map<String, Object>>> NEW_MAP = str -> new HashMap<>();

    public static DataStoreComparerResult compare(DataStore a, DataStore b) {
        DataStoreComparer comparer = new DataStoreComparer();
        DataStoreComparerResult result = new DataStoreComparerResult(a, b);
        comparer.compare(a, b, result, true);
        comparer.compare(a, b, result, false);
        return result;
    }

    private void compare(DataStore a, DataStore b, DataStoreComparerResult result, boolean checkDifferences) {
        Set<Attribute> commonAttributes = a.getCommonAttributes(b);

        for (Map.Entry<String, Map<Attribute, Object>> entry : a.getEntries().entrySet()) {
            String id = entry.getKey();
            Map<Attribute, Object> attsA = entry.getValue();
            Map<Attribute, Object> attsB = b.getEntries().get(id);
            if (attsB == null) {
                result.addMissing(b.getName(), id);
            } else if (checkDifferences) {
                // Check what's in A should be in B and the same
                for (Map.Entry<Attribute, Object> attEntryA : attsA.entrySet()) {
                    Attribute att = attEntryA.getKey();
                    if (!commonAttributes.contains(att)) {
                        continue;
                    }
                    Object valueA = attEntryA.getValue();
                    Object valueB = attsB.get(att);
                    if (!Objects.equals(valueA, valueB)) {
                        result.addDifference(id, att, Map.of(a.getName(), valueA, b.getName(), valueB == null ? MISSING_VALUE : valueB));
                    }
                }

                // Check what's in B should not be missing from A
                for (Map.Entry<Attribute, Object> attEntryB : attsB.entrySet()) {
                    Attribute att = attEntryB.getKey();
                    if (!commonAttributes.contains(att)) {
                        continue;
                    }
                    if (!attsA.containsKey(att)) {
                        result.addDifference(id, att, Map.of(a.getName(), MISSING_VALUE, b.getName(), attEntryB.getValue()));
                    }
                }
            }
        }
    }

    @Data
    public static class DataStoreComparerResult {
        private final DataStore dataStore1;
        private final DataStore dataStore2;
        /**
         * dataStoreName -> list of missing ids in that data store
         */
        private final Map<String, List<String>> missing = new HashMap<>();
        /**
         * id -> map of fieldName -> map -> dataStoreName, value
         */
        private final Map<String, Map<Attribute, Map<String, Object>>> differences = new HashMap<>();
        private Map<String, Integer> dataStoreStats = new HashMap<>();

        private DataStoreComparerResult(DataStore dataStore1, DataStore dataStore2) {
            this.dataStore1 = dataStore1;
            this.dataStore2 = dataStore2;
        }

        private void addMissing(String dataStoreName, String id) {
            missing.computeIfAbsent(dataStoreName, str -> new ArrayList<>()).add(id);
        }

        private void addDifference(String id, Attribute attribute, Map<String, Object> values) {
            differences.computeIfAbsent(id, NEW_MAP)
                    .computeIfAbsent(attribute, str -> new HashMap<>())
                    .putAll(values);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(dataStore1).append('\n');
            sb.append(dataStore2).append('\n');

            for (Map.Entry<String, List<String>> entry : missing.entrySet()) {
                String dataStoreName = entry.getKey();
                List<String> ids = entry.getValue();
                sb.append(String.format("%d missing from %s:%n%s%n", ids.size(), dataStoreName, String.join("\n", ids)));
            }
            sb.append("----------------------------------------------------\n");
            sb.append("Attributes compared: ").append(dataStore1.getCommonAttributes(dataStore2)).append('\n');
            sb.append(differences.isEmpty() ? "No" : differences.size()).append(" objects with differences in field values\n");
            for (Map.Entry<String, Map<Attribute, Map<String, Object>>> entry : differences.entrySet()) {
                String id = entry.getKey();
                sb.append(String.format("id %s differences:%n", id));
                for (Map.Entry<Attribute, Map<String, Object>> diffs : entry.getValue().entrySet()) {
                    Attribute attr = diffs.getKey();
                    Map<String, Object> dataStoreNameToValue = diffs.getValue();
                    sb.append(String.format("\t%s: %s %s%n", attr, dataStoreNameToValue,
                            attr.getHandler().describeDifference(dataStoreNameToValue.entrySet())));
                }
            }
            return sb.toString();
        }
    }

}
