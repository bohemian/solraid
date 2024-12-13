A POC implementation specifically to address this: https://hello.atlassian.net/wiki/spaces/CloudSecurity/pages/3453559443/MAS+-+Data+Integrity+Problem
but that can be used to compare any two datastores that should be consistent.

## How to run

This POC only runs locally. You'll have to fetch data using the ES query UI and save (copy-paste entire response) it locally. 

Call `Runner.main` passing four params:

1. the short name of the datastore A ("prs" , "access" or "apm")
1. the local directory to find files containing datastore A items
1. the short name of the datastore B ("prs" , "access" or "apm")
1. the local directory to find files containing datastore B items

Differences (see below) are printed to STDOUT.

## How it works

Datastores definitions (currently separate classes) define:
- the fields they store (chosen from an enum) and their paths
- the path to the id field

Eventually (and fairly simply) data stores can be entirely defined in config.

Datastores then load all items within an id range

The DataStoreComparer compares the two (in linear time) reporting:
- items in A, but missing from B
- items in B, but missing from A
- items in both, but have different values for fields (only fields common to both are checked)
