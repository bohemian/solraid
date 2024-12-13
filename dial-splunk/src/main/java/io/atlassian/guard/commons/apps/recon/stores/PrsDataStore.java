package io.atlassian.guard.commons.apps.recon.stores;

import java.time.Instant;
import java.util.Map;

import static io.atlassian.guard.commons.apps.recon.Attribute.LAST_MODIFIED;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class PrsDataStore {
//
//    public PrsDataStore() {
//        super("PRS", "$._id", Map.of(
//                LAST_MODIFIED, "$._source.data.payload.response.user.last_updated"
//        ));
//    }

    // https://access-user-raw.sgw.staging.atl-paas.net/_dashboards/app/dev_tools#/console
    public String getQuery(String aboveId, Instant fromInclusive, Instant toExclusive) {
        String query = """
                GET /raw-prs-user/_search
                {
                  "from": 0,
                  "size": 10000,
                  "track_total_hits": true,
                  "query": {
                    "bool": {
                      "must": [
                        {
                          "range": {
                            "id": {
                              "gt": "%s"
                            }
                          }
                        },
                          "range": {
                            "source_sequence": {
                              "gte": "%d",
                              "lt": "%d"
                            }
                          }
                        {
                          "bool": {
                            "must_not": [
                              {
                                "regexp": {
                                  "data.payload.response.user.email": ".*connect.atlassian.com"
                                }
                              }
                            ]
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

        return String.format(query, aboveId, ISO_INSTANT.format(fromInclusive), ISO_INSTANT.format(toExclusive));
    }
}


