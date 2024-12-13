package io.atlassian.guard.commons.apps.recon;

import io.atlassian.guard.commons.apps.recon.DataStoreComparer.DataStoreComparerResult;
import io.atlassian.guard.commons.apps.recon.stores.AccessUserDataStore;
import io.atlassian.guard.commons.apps.recon.stores.ApmDataStore;
import io.atlassian.guard.commons.apps.recon.stores.DataStore;
import lombok.SneakyThrows;

import java.time.Instant;
import java.util.Map;

public class Runner {
    // For looking up concrete class from program params
    private static final Map<String, DataStore> DATA_STORES = Map.of(
//            "prs", new PrsDataStore(),
            "access", new AccessUserDataStore(),
            "apm", new ApmDataStore()
    );

    public static void main(String[] args) {
        try {
            DataStore a = new AccessUserDataStore();
            DataStore b = new ApmDataStore();
            new Runner().compare(a, b);
        } catch (Exception e) {
            System.out.println("Runner caught: " + e);
        }
    }

    private void compare(DataStore a, DataStore b) {
        String fromId = "";
        Instant fromInstant = Instant.ofEpochSecond(946_684_800);  // 2000
        Instant toInstant = Instant.ofEpochSecond(2_000_000_000); // 2033
        for (int i = 0; i < 4; i++) {
            System.out.println("Iteration " + i + " fromId=" + fromId);
            if (moreThan(a.getMinId(), fromId)) {
                System.out.println("skipping fetch for A because " + fromId + " is not more than " + a.getMinId() );
            } else {
                a.fetch(fromId, fromInstant, toInstant);
            }
            if (moreThan(b.getMinId(), fromId)) {
                System.out.println("skipping fetch for B because " + fromId + " is not more than " + b.getMinId() );
            } else {
                b.fetch(fromId, fromInstant, toInstant);
            }
            DataStoreComparerResult result = DataStoreComparer.compare(a, b);
            System.out.println(result);
            System.out.println("A: " + a);
            System.out.println("B: " + b);
            fromId = lesser(a.getMaxId(), b.getMaxId());
            System.out.println(fromId);
        }
    }

    private static String lesser(String id1, String id2) {
        return id1.compareTo(id2) < 0 ? id1 : id2;
    }

    private static boolean moreThan(String id1, String id2) {
        return id1.compareTo(id2) > 0;
    }

    @SneakyThrows
    public static void mainOld(String[] args) {
        if (args.length < 4) {
            System.err.println("Usage: Runner <name1> <filename1> <name2> <filename2>");
            System.exit(1);
        }

        String name1 = args[0];
        String dirName1 = args[1];
        String name2 = args[2];
        String dirName2 = args[3];

        DataStore a = DATA_STORES.get(name1.toLowerCase()).addFromDirectory(dirName1);
        DataStore b = DATA_STORES.get(name2.toLowerCase()).addFromDirectory(dirName2);

        DataStoreComparerResult result = DataStoreComparer.compare(a, b);

        System.out.println(result);
    }

}
