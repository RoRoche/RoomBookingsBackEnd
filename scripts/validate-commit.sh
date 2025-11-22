#!/bin/sh

message=$(cat "$1")

if ! echo "$message" | grep -Eq "#[0-9]+"; then
  echo "❌ Commit rejected : commit message must reference an issue (e.g. 'Fixes #12')"
  exit 1
fi
