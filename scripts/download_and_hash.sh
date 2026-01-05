#!/usr/bin/env bash
# Simple helper to download a file and print its sha256
set -euo pipefail

if [ $# -lt 1 ]; then
  echo "Usage: $0 <url> [output-filename]"
  exit 2
fi

URL="$1"
OUT=${2:-$(basename "$URL")}

echo "Downloading $URL -> $OUT"
curl -L --progress-bar -o "$OUT" "$URL"

echo "Computing sha256..."
sha256sum "$OUT"

echo "Done. You can copy the sha256 into models_manifest.json for reliable verification."
