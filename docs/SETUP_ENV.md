# Environment Setup — Android / Local models

This document lists minimal environment steps to develop and run the Platinum Arabic AI Assistant locally.

1) Java
- Install OpenJDK 11 or newer.
- Verify:
```bash
java -version
```

2) Android SDK & Command-line Tools
- Install Android SDK and command-line tools (sdkmanager, avdmanager, emulator).
- Set `ANDROID_SDK_ROOT` or `ANDROID_HOME` to SDK path.
- Example (bash):
```bash
export ANDROID_SDK_ROOT=$HOME/Android/Sdk
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/platform-tools:$PATH"
```

3) Create an AVD (if you need an emulator)
```bash
sdkmanager "system-images;android-30;google_apis;x86_64"
avdmanager create avd -n test_avd -k "system-images;android-30;google_apis;x86_64"
```

4) Gradle
- Use the included Gradle wrapper: `./gradlew` (no system Gradle required).

5) Native/local AI models
- This project targets local models (Whisper/Piper/BERT). Models are large and may require separate downloads.
- Keep model files under the app's `models/` directory or in a user data folder.
- Plan for disk usage and optional model manager to download models on demand.

6) Hardware recommendations (development)
- For local model testing: CPU with AVX2 or a GPU-supported setup; 8+ GB RAM recommended for lightweight models; 16+ GB for heavier models.

7) Troubleshooting
- If `emulator` command not found, ensure SDK `emulator` path is on `PATH`.
- `adb` problems: run `adb devices` and restart server: `adb kill-server && adb start-server`.
