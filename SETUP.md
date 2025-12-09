# Getting Started - Development Setup Guide

## 🚀 Prerequisites

Before you begin, ensure you have:

1. **Java Development Kit (JDK)**
   - Java 17 or higher
   - Download from: https://www.oracle.com/java/technologies/downloads/

2. **Android Studio**
   - Version Arctic Fox (2021.1.1) or newer
   - Download from: https://developer.android.com/studio

3. **Android SDK**
   - API Level 34 (Android 14) for compilation
   - API Level 26 (Android 8.0) for minimum device support
   - Installed via Android Studio SDK Manager

4. **Git**
   - For version control
   - Download from: https://git-scm.com/

## 📦 Installation Steps

### 1. Clone the Repository

```bash
# Using HTTPS
git clone https://github.com/your-username/platinum-assistant.git
cd platinum-assistant

# Or using SSH
git clone git@github.com:your-username/platinum-assistant.git
cd platinum-assistant
```

### 2. Setup Android Environment

```bash
# Set ANDROID_HOME (Linux/macOS)
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator:$ANDROID_HOME/tools

# Or on Windows
set ANDROID_HOME=C:\Users\YourUsername\AppData\Local\Android\sdk
set PATH=%PATH%;%ANDROID_HOME%\emulator;%ANDROID_HOME%\tools
```

### 3. Install Dependencies

```bash
# The Gradle wrapper will automatically download Gradle
./gradlew build
```

### 4. Run on Emulator

```bash
# Create Android Virtual Device (AVD) in Android Studio first
# Then run:
./gradlew installDebug
./gradlew run
```

### 5. Run on Physical Device

```bash
# Connect device via USB and enable Developer Options
./gradlew installDebug
```

## 🛠️ Development Workflow

### Building the App

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Install and run
./gradlew installDebug
./gradlew run
```

### Running Tests

```bash
# Unit tests
./gradlew test

# UI tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Coverage report
./gradlew testDebugUnitTest jacocoTestDebugUnitTestReport
```

### Code Analysis

```bash
# Lint check
./gradlew lint

# View lint report
# Open: app/build/reports/lint-results-debug.html
```

### Debugging

```bash
# Enable debug mode and connect debugger in Android Studio
# Or use ADB logcat:
adb logcat | grep platinum
```

## 🗂️ Project Structure Overview

```
platinum-assistant/
├── .github/
│   └── copilot-instructions.md    # Development guidelines
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/platinumassistant/
│   │   │   │   ├── core/              # DI, Security, Voice, AI
│   │   │   │   ├── data/              # Database, API, Repositories
│   │   │   │   ├── domain/            # Entities, UseCases, Repositories
│   │   │   │   ├── ui/                # Theme, Components, Screens
│   │   │   │   └── features/          # Feature modules
│   │   │   ├── res/                   # Resources (strings, colors, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                      # Unit tests
│   │   └── androidTest/               # UI tests
│   ├── build.gradle.kts               # App dependencies
│   └── proguard-rules.pro             # ProGuard rules
├── build.gradle.kts                   # Root build config
├── settings.gradle.kts                # Project settings
├── gradle/                            # Gradle configuration
├── README.md                          # Project overview
├── ARCHITECTURE.md                    # Architecture documentation
├── ROADMAP.md                         # Development roadmap
├── CONTRIBUTING.md                    # Contribution guidelines
└── LICENSE                            # AGPLv3 License
```

## 📝 Key Files to Understand

1. **settings.gradle.kts**
   - Project name and module configuration
   - Root gradle settings

2. **app/build.gradle.kts**
   - App dependencies
   - Build configuration
   - Target SDK and minimum SDK
   - Build types (debug/release)

3. **app/src/main/AndroidManifest.xml**
   - App permissions
   - Activities, services, receivers
   - App metadata

4. **app/src/main/java/com/platinumassistant/PlatinumApplication.kt**
   - App initialization
   - Logging setup
   - Global configuration

5. **ARCHITECTURE.md**
   - Design patterns used
   - Layer descriptions
   - Data flow patterns

## 🔐 Security Considerations

### Development Security
- Never commit API keys or secrets
- Use .gitignore for sensitive files
- Use secure storage for keys in code

### Building Release APK
```bash
# Create keystore (one-time)
keytool -genkey -v -keystore ~/platinum-key.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias platinum-key

# Build signed release APK
./gradlew assembleRelease
```

## 🐛 Common Issues & Solutions

### Issue: Gradle build fails
```bash
# Solution: Clear cache and rebuild
./gradlew clean build
```

### Issue: ADB not found
```bash
# Solution: Ensure ANDROID_HOME is set correctly
echo $ANDROID_HOME
```

### Issue: Emulator very slow
```bash
# Solution: Use hardware acceleration
# Enable in Android Virtual Device settings:
# Graphics: Hardware - GLES 2.0
```

### Issue: Insufficient permissions
```bash
# Solution: Rebuild and reinstall
./gradlew uninstallDebug
./gradlew installDebug
```

## 📚 Learning Resources

- [Android Developers Documentation](https://developer.android.com/docs)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVVM Pattern](https://developer.android.com/jetpack/guide)

## 🤝 Contributing

Before contributing:
1. Read CONTRIBUTING.md
2. Create a feature branch
3. Follow code standards
4. Write tests
5. Submit pull request

## 📞 Support & Community

- **GitHub Issues**: Report bugs and request features
- **Discussions**: Ask questions and discuss ideas
- **Telegram**: Join community chat
- **Email**: support@platinumassistant.com

## 🎯 Next Steps

1. **Setup Your Environment**: Follow the installation steps above
2. **Explore the Code**: Start with MainActivity.kt
3. **Read Architecture**: Understand the project structure
4. **Run Tests**: Verify everything works
5. **Start Coding**: Pick an issue from GitHub and contribute

---

**Happy coding! Let's build something amazing together! 🚀**
