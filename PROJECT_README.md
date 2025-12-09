# Platinum Arabic AI Assistant

🏆 **The world's first comprehensive offline-first Arabic AI voice assistant with 50+ personalities**

> 100% Free • Privacy-First • Open Source • No Ads • No Subscriptions

## Overview

Platinum is an advanced Android application that brings a powerful, offline-capable AI assistant to your device. With over 50 unique personalities, multi-language support, and 100+ integrated features, Platinum is designed to be your personal AI companion.

### Key Features

✨ **50+ Unique Personalities**
- Technical experts (Jarvis, Friday, Elon Musk, Steve Jobs, etc.)
- Entertaining characters (Deadpool, Joker, SpongeBob, etc.)
- Iconic heroes (Batman, Superman, Iron Man, etc.)
- Arabic scholars and poets
- Historical figures (Einstein, Tesla, Marie Curie, etc.)
- Fully customizable personalities

🔐 **Privacy & Security**
- 100% offline processing (no data leaves your device)
- AES-256 encryption for all local data
- No tracking, no ads, no subscriptions
- Open source and fully transparent

🎤 **Voice Features**
- Arabic speech recognition (Whisper model)
- Natural speech synthesis (Piper TTS)
- 10+ language support
- Real-time emotion detection

💼 **Personal Assistant**
- Task and reminder management
- Calendar integration
- Daily briefings
- Mood tracking
- Habit management

👨‍💻 **Programmer Assistant**
- Code generation (25+ languages)
- Code explanation and analysis
- Debugging support
- Git integration
- Performance analysis

🎮 **Entertainment**
- Interactive games and puzzles
- Story mode
- Daily challenges
- Music and podcast integration

## Architecture

Platinum follows Clean Architecture and MVVM patterns:

```
app/
├── core/           # DI, Security, Voice processing, AI utilities
├── data/           # Local & remote data access, repositories
├── domain/         # Business logic, entities, use cases
├── ui/             # Jetpack Compose UI, themes, components
└── features/       # Feature-specific modules (Assistant, Personalities, Programmer, etc.)
```

## Tech Stack

- **Language**: Kotlin 100%
- **UI**: Jetpack Compose + Material Design 3
- **Database**: Room + SQLCipher (AES-256)
- **Storage**: DataStore + Encrypted SharedPreferences
- **DI**: Hilt/Dagger 2
- **Architecture**: Clean Architecture + MVVM
- **AI Models**: Whisper (ASR), Piper (TTS), BERT (NLU)
- **Testing**: JUnit, Mockito, Espresso

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Android SDK API 26+
- Java JDK 11+
- Gradle 8.0+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/slto7-code-fixer/jarvis.git
cd jarvis
```

2. Open in Android Studio:
```bash
code /path/to/jarvis
```

3. Build the project:
```bash
./gradlew assembleDebug
```

4. Run on device/emulator:
```bash
./gradlew installDebug
```

## Folder Structure

- `.vscode/` - VS Code configuration (tasks, launch, extensions)
- `docs/` - Documentation (setup, architecture, etc.)
- `scripts/` - Helper scripts (setup checks, CI helpers)
- `.github/workflows/` - GitHub Actions CI/CD pipelines
- `app/` - Main Android application

## Personalities

### Technical (8)
Jarvis, Friday, Elon Musk, Steve Jobs, Mark Zuckerberg, Bill Gates, Tim Cook, Sundar Pichai

### Comedy (6)
Deadpool, Joker, Minnie, Bugs Bunny, SpongeBob, Freddy

### Heroes (10)
Wolverine, Venom, Thanos, Hulk, Iron Man, Captain America, Batman, Superman, Wonder Woman, Spider-Man

### Arabic (8)
Scholar, Philosopher, Poet, Doctor, Merchant, Teacher, Leader, Storyteller

### Historical (6)
Einstein, Newton, Darwin, Tesla, Marie Curie, Aristotle

## Languages Supported

**UI Languages**: Arabic, English, French, Spanish, German, Russian, Chinese, Japanese, Korean, Turkish

**Voice Languages**: Arabic (with dialects), English, French, Spanish, German

**Programming Languages**: Python, JavaScript, TypeScript, Java, C++, C#, PHP, Ruby, Go, Rust, Swift, Kotlin, Dart, SQL, HTML/CSS, Bash, PowerShell, R, MATLAB, Julia, Scala, and more

## Security

All data is encrypted using AES-256 at rest. The application:
- Does not connect to external servers for core functionality
- Does not collect personal data
- Does not use analytics or tracking
- Is fully open source for verification

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Roadmap

- **v1.0**: Core features (chat, personalities, basic assistant)
- **v2.0**: Advanced features (programmer assistant, health tracking)
- **v3.0**: iOS support, cloud sync (optional)
- **v4.0**: Desktop client
- **v5.0**: AR/VR support

See [ROADMAP.md](ROADMAP.md) for detailed timeline.

## License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

## Support

- **GitHub Issues**: Report bugs and request features
- **Discussions**: Join community discussions
- **Email**: Support email (to be added)
- **Telegram**: Community channel (to be added)

## Acknowledgments

- OpenAI for Whisper ASR model
- Meta for voice synthesis
- Google for BERT NLU model
- Android community for excellent libraries and tools

---

**Made with ❤️ for privacy, freedom, and Arabic**
