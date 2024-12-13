#!/usr/bin/env bash

if ! grep -q SNAPSHOT gradle.properties; then
  echo "Library is a snapshot version. Proceeding to publish step."
else
  echo "Library is not a snapshot version. Skipping publish step."
  exit 0
fi
