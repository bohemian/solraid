package io.atlassian.guard.commons.apps.recon.stores;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static io.atlassian.guard.commons.apps.recon.Attribute.LAST_MODIFIED;
import static io.atlassian.guard.commons.apps.recon.Attribute.POLICY_ID;

public class AccessUserDataStore extends DataStore {

    // https://dps-access.sgw.staging.atl-paas.net/_dashboards/app/dev_tools#/console
    // GET /access-user/_search

    private static final String COMMAND = "atlas micros resource elasticsearch query run -r dps-access -s dps-access -e stg-east --endpoint access-user/_search --method GET -b '%s'";
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
                        "business_key": {
                          "gt": "%s"
                        }
                      }
                    },
                    {
                      "range": {
                        "profile_last_modified": {
                          "gte": "%d",
                          "lt": "%d"
                        }
                      }
                    },
                    {
                      "bool": {
                        "must_not": [
                          {
                            "match": {
                              "primary_domain": "connect.atlassian.com"
                            }
                          }
                        ]
                      }
                    }
                  ]
                }
              },
              "sort": [
                "business_key"
              ]
            }
            """;

    public AccessUserDataStore() {
        super("ACU", "$._id", Map.of(
                LAST_MODIFIED, "$._source.profile_last_modified",
                POLICY_ID, "$._source.authentication_policy.policy_id"
        ));
    }

    @Override
    public String getCommand(String aboveId, Instant fromInclusive, Instant toExclusive) {
        String query = String.format(QUERY, aboveId, fromInclusive.toEpochMilli(), toExclusive.toEpochMilli());
        return String.format(COMMAND, query);
    }

}
