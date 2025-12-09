#!/usr/bin/env bash
set -euo pipefail

echo "Local environment quick check for Platinum Arabic AI Assistant"

echo "\n1) Java"
if command -v java >/dev/null 2>&1; then
  java -version 2>&1 | head -n1
else
  echo "  java not found. Install OpenJDK 11+ (e.g. sudo apt install openjdk-11-jdk)"
fi

echo "\n2) Gradle / Gradle Wrapper"
if [ -x ./gradlew ]; then
  echo "  gradlew present."
  ./gradlew --version || true
elif command -v gradle >/dev/null 2>&1; then
  echo "  system gradle present: $(gradle --version | head -n1)"
else
  echo "  No gradle found. Consider installing via SDKMAN or apt, or generate wrapper on a machine with Gradle."
  echo "  SDKMAN quick install: curl -s \"https://get.sdkman.io\" | bash"
fi

echo "\n3) Android SDK (basic checks)"
if [ -n "${ANDROID_SDK_ROOT-}" ] || [ -n "${ANDROID_HOME-}" ]; then
  echo "  ANDROID_SDK_ROOT or ANDROID_HOME is set"
  echo "  ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT-}"
else
  echo "  Android SDK env vars not set. If you plan to build locally you need to set ANDROID_SDK_ROOT and add sdk tools to PATH. See docs/SETUP_ENV.md"
fi

echo "\n4) Quick JSON validation for .vscode files"
python -m json.tool .vscode/tasks.json > /dev/null && echo "  tasks.json OK" || echo "  tasks.json invalid"
python -m json.tool .vscode/launch.json > /dev/null && echo "  launch.json OK" || echo "  launch.json invalid"
python -m json.tool .vscode/extensions.json > /dev/null && echo "  extensions.json OK" || echo "  extensions.json invalid"

echo "\nDone. For full APK builds prefer using CI or a machine with Gradle and Android SDK (recommended for weak devices)."
