# Platinum Arabic AI Assistant - Development Instructions

## Project Overview
This is an advanced Android Kotlin application for the Platinum Arabic AI Assistant - a comprehensive, offline-first voice assistant with 50+ personalities, multi-language support, AES-256 encryption, and 100+ integrated features.

## Key Principles
1. **Privacy First**: All processing is local, no data leaves the device
2. **Free Forever**: No subscriptions, ads, or in-app purchases
3. **Offline Capable**: All core features work without internet
4. **High Quality**: Clean Architecture with MVVM pattern
5. **Performance**: Optimized for devices with limited resources

## Architecture
- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose with Material Design 3
- **Database**: Room + SQLCipher (AES-256)
- **Storage**: DataStore + Encrypted SharedPreferences
- **AI Models**: Local (Whisper, Piper, BERT for Arabic)

## Project Structure
```
app/
├── src/main/
│   ├── java/com/platinumassistant/
│   │   ├── core/        # DI, Security, Voice, AI, Utils
│   │   ├── data/        # Local, Remote, Repositories
│   │   ├── domain/      # Entities, UseCases
│   │   ├── ui/          # Theme, Components, Screens
│   │   └── features/    # Feature-specific modules
│   └── res/             # Resources with multi-language support
└── build.gradle.kts     # Project configuration
```

## Development Guidelines

### Code Standards
- Follow Kotlin conventions and best practices
- Use SOLID principles throughout
- Implement proper error handling
- Add comprehensive documentation
- Write unit tests (target > 90% coverage)

### Security Requirements
- All sensitive data encrypted
- No hardcoded credentials
- Secure random generation
- Regular security audits
- Dependency vulnerability scanning

### Performance Targets
- Response time < 500ms
- Crash rate < 0.1%
- Memory usage < 500MB
- Battery drain < 5%/hour
- Compatibility with Android 8.0+

### Language Support
- Arabic (primary with multiple dialects)
- English, French, Spanish, German, Russian
- Chinese Simplified, Japanese, Korean
- Full RTL support for Arabic

## Feature Implementation Priority
1. **Phase 1**: Core voice processing, personalities, basic UI
2. **Phase 2**: Personal assistant, memory system, daily features
3. **Phase 3**: Programmer assistant, health, education, entertainment
4. **Phase 4**: Performance optimization and testing
5. **Phase 5**: Release preparation

## Build & Deployment
- Target Android 8.0+ (API 26)
- Support 95% of devices
- Google Play Store primary distribution
- GitHub Releases for open-source version
- F-Droid for free software variant

## Testing Requirements
- Unit tests: All core logic
- Integration tests: Database, voice processing
- UI tests: User flows
- Performance tests: Battery, memory, CPU
- Security tests: Encryption, data protection

## Contact & Support
- GitHub Issues for bugs and features
- Telegram community for discussions
- Email support for urgent issues
