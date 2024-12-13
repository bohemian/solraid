package io.atlassian.guard.commons.apps.recon.stores;

import com.fasterxml.jackson.core.JsonToken;
import io.atlassian.guard.commons.apps.recon.Attribute;
import io.atlassian.guard.commons.utils.HandyUtils;
import io.atlassian.guard.commons.utils.JsonUtils;
import lombok.Data;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

@Data
public abstract class DataStore {
    private final String name;
    private final String idFieldPath;
    private final Map<Attribute, String> fieldPaths;
    private final Map<String, Map<Attribute, Object>> entries = new HashMap<>(10000);

    @SneakyThrows
    public DataStore addFromDirectory(String directory) {
        Files.find(Paths.get(directory), 1, (p, b) -> b.isRegularFile())
                .forEach(p -> addFromFile(p.toAbsolutePath().toString()));
        return this;
    }

    @SneakyThrows
    public DataStore addFromFile(String filename) {
        return addFrom(HandyUtils.fileToString(filename));
    }

    @SneakyThrows
    public void fetch(String fromId, Instant fromInclusive, Instant toExclusive) {
        entries.clear();
        String command = getCommand(fromId, fromInclusive, toExclusive);
        command = command.replaceAll("\\R", " ");
        ProcessBuilder builder = new ProcessBuilder();
        List<String> cmdParts = List.of("sh", "-c", command);
        String[] array = cmdParts.toArray(String[]::new);
        builder.command(array).redirectErrorStream(true);
        long start = System.currentTimeMillis();
        Process process = builder.start();
        long duration = System.currentTimeMillis() - start;
        System.out.println("Query time " + duration);

        Thread.sleep(200);

        var response = new Scanner(process.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\Z").next();
        System.out.println(getName() + " start ==================================================");
        System.out.println("fromId:" + fromId + "->"+command);
        System.out.println(response);
        System.out.println(getName() + " end ==================================================");

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            System.out.println(response);
            if (response.contains("ERROR")) {
                System.out.println("Check auth for:\n" + command);
            }
            throw new RuntimeException("Exit code " + exitCode);
        }

        addFrom(response);
        System.out.println("Loaded " + entries.size());
    }

    @SneakyThrows
    public DataStore addFrom(String input) {
        Scanner scanner = new Scanner(input).useDelimiter("\"hits\": \\{\\s*\"hits\": \\[|,\\s+(?=\\{\\s+\"_id\")|\\W+\\Z");
        scanner.next(); // skip header
        while (scanner.hasNext()) {
            String dataJson = scanner.next();
            if (dataJson.isBlank()) {
                break;
            }
            try {
                JsonUtils.get(dataJson, idFieldPath);
            } catch (Exception e) {
                System.out.println(dataJson);
            }
            String id = JsonUtils.get(dataJson, idFieldPath);
            Map<Attribute, Object> attributeValues = new HashMap<>();
            fieldPaths.forEach((a, p) -> attributeValues.put(a, a.getHandler().rectify(JsonUtils.get(dataJson, p))));
            entries.put(id, attributeValues);
        }
        scanner.close();
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s: size=%d, min=%s, max=%s%n%s", name, entries.size(), getMinId(), getMaxId(),  String.join("\n", entries.keySet()));
    }

    public String getMinId() {
        if (entries.isEmpty()) {
            return "";
        }
        return Collections.min(entries.keySet());
    }

    public String getMaxId() {
        if (entries.isEmpty()) {
            return "";
        }
        return Collections.max(entries.keySet());
    }

    public abstract String getCommand(String aboveId, Instant fromInclusive, Instant toExclusive);

    public Set<Attribute> getCommonAttributes(DataStore that) {
        Set<Attribute> commonAttributes = new HashSet<>(getFieldPaths().keySet());
        commonAttributes.retainAll(that.getFieldPaths().keySet());
        return commonAttributes;
    }


}
