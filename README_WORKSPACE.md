# Platinum Arabic AI Assistant — Workspace Quick Start

This file explains quick steps to set up and run the project in VS Code.

Prerequisites
- Java JDK 11+ installed and `java` on PATH
- Android SDK and `sdkmanager`/`emulator` available
- Android Studio or command-line SDK tools
- `./gradlew` is used to build (Gradle wrapper included)

Common commands

Build debug APK:
```bash
./gradlew assembleDebug
```

Install to connected device or emulator:
```bash
./gradlew installDebug
```

Run unit tests:
```bash
./gradlew testDebugUnitTest
```

Run instrumentation tests (device/emulator required):
```bash
./gradlew connectedAndroidTest
```

Start default emulator (if AVDs installed):
```bash
emulator -list-avds | head -n1 | xargs -I % emulator -avd % &
```

VS Code
- Open this folder: `code /home/omar/Documents/jarvis`
- Recommended extensions are in `.vscode/extensions.json`.
- Use the `Terminal > Run Task...` menu to run Gradle tasks from `.vscode/tasks.json`.

Next steps
- Install Android SDK and an AVD.
- Run `./gradlew assembleDebug` to verify build.
- Use the `Attach to Android App` launch configuration to debug on device/emulator.
