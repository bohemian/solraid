package io.atlassian.guard.commons.apps.recon.stores;

import java.time.Instant;
import java.util.Map;

import static io.atlassian.guard.commons.apps.recon.Attribute.LAST_MODIFIED;
import static io.atlassian.guard.commons.apps.recon.Attribute.POLICY_ID;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class ApmDataStore extends DataStore {
    // https://access-user-raw.sgw.staging.atl-paas.net/_dashboards/app/dev_tools#/console
    // GET /raw-identity-user-auth-policy-mapping/_search

    private static final String COMMAND = "atlas micros resource elasticsearch query run -r access-user-raw -s access-user-raw -e stg-east --endpoint raw-identity-user-auth-policy-mapping/_search --method GET -b '%s'";
    private static final String QUERY = """
            {
              "from": 0,
              "size": 10000,
              "track_total_hits": true,
              "query": {
                "bool": {
                  "must": [
                    {
                      "range": {
                        "data.payload.user_id": {
                          "gt": "%s"
                        }
                      }
                    },
                    {
                      "range": {
                        "source_sequence": {
                          "gte": "%d",
                          "lt": "%d"
                        }
                      }
                    }
                  ]
                }
              },
              "sort": [
                "id"
              ]
            }
            """;

    public ApmDataStore() {
        super("APM", "$._id", Map.of(
                POLICY_ID, "$._source.data.payload.policy_id"
        ));
    }

    public String getQuery(String aboveId, Instant fromInclusive, Instant toExclusive) {
        return String.format(QUERY, aboveId, ISO_INSTANT.format(fromInclusive), ISO_INSTANT.format(toExclusive));
    }

    @Override
    public String getCommand(String aboveId, Instant fromInclusive, Instant toExclusive) {
        String query = String.format(QUERY, aboveId, fromInclusive.toEpochMilli(), toExclusive.toEpochMilli());
        return String.format(COMMAND, query);
    }
}


