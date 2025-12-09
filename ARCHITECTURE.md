# Project Architecture Overview

## Layer Architecture

### Domain Layer
- **Purpose**: Business logic and entity definitions
- **Location**: `domain/`
- **Contains**:
  - `entities/`: Data models (Personality, Task, Message, etc.)
  - `repositories/`: Interface definitions for data access
  - `usecases/`: Business logic operations

### Data Layer
- **Purpose**: Data access and external communication
- **Location**: `data/`
- **Contains**:
  - `local/`: Room database, DAOs, SQLCipher integration
  - `remote/`: API clients, network models
  - `repositories/`: Repository implementations

### UI Layer
- **Purpose**: User interface and presentation
- **Location**: `ui/`
- **Contains**:
  - `theme/`: Material Design 3 theme configuration
  - `components/`: Reusable Compose components
  - `screens/`: Feature screens
  - `navigation/`: Navigation graphs and handlers

### Core Layer
- **Purpose**: Cross-cutting concerns
- **Location**: `core/`
- **Contains**:
  - `di/`: Dependency Injection modules (Hilt)
  - `security/`: Encryption and security utilities
  - `voice/`: Voice processing (Whisper, Piper integration)
  - `ai/`: Local ML models
  - `utils/`: Common utilities

### Features Layer
- **Purpose**: Feature-specific implementations
- **Location**: `features/`
- **Contains**:
  - `personalities/`: Personality management
  - `assistant/`: Personal assistant functionality
  - `programmer/`: Programmer tools
  - `health/`: Health tracking
  - `entertainment/`: Games and entertainment

## Data Flow Pattern

```
User Action (UI)
    ↓
ViewModel / Presenter
    ↓
Use Case
    ↓
Repository
    ↓
Data Source (Local/Remote)
    ↓
Domain Entity
    ↓
UI Model (State)
    ↓
Compose UI Update
```

## Dependency Injection Strategy

Using **Hilt** for dependency injection:
- Application singleton scope for database, preferences, encryption
- Activity/Fragment scope for UI components
- Custom scopes for features as needed

## Testing Strategy

### Unit Tests
- Test each use case independently
- Mock repositories and data sources
- Location: `src/test/java/`

### Integration Tests
- Test repository with database
- Test feature interactions
- Location: `src/androidTest/java/`

### UI Tests
- Test screens and navigation
- Test user interactions
- Use Compose UI testing framework

## Database Schema

### SQLCipher Integration
- All databases encrypted with AES-256
- Master key generated from device security
- Automatic encryption/decryption

### Tables (to be implemented)
- `personalities`: Personality data
- `tasks`: Task and todo items
- `messages`: Chat history
- `settings`: User preferences
- `statistics`: Usage analytics

## Security Architecture

### Data Encryption
1. **At Rest**: SQLCipher (AES-256)
2. **In Transit**: TLS 1.3
3. **In Memory**: Secure deletion after use

### Permission Management
- Runtime permissions with clear explanations
- Minimum permissions principle
- Permission-less fallback options

### Code Protection
- ProGuard obfuscation in release builds
- Code signing with developer certificate
- Integrity checks on startup

## Performance Optimization

### Memory Management
- Efficient list layouts with LazyColumn
- Image caching with appropriate sizes
- Database query optimization with indexes

### Battery Optimization
- Power modes: Ultra Saver, Saver, Balanced, Performance
- Adaptive refresh rates
- Intelligent background tasks

### Network Optimization
- Offline-first design
- Compression of data
- Batch requests when possible

## Scalability Considerations

### Feature Addition
1. Create feature package in `features/`
2. Define entities in `domain/entities/`
3. Create repository interface in `domain/repositories/`
4. Implement repository in `data/repositories/`
5. Create use cases in `domain/usecases/`
6. Create UI screens in `ui/screens/` or feature package
7. Register in DI module

### Modularization Future
- Split into feature modules (app-personalities, app-assistant, etc.)
- Create shared modules (app-core, app-ui)
- Separate app module for app-specific code

## Navigation Structure

Using Jetpack Compose Navigation:
- Single activity (MainActivity)
- Navigation graph defined in code
- Deep linking support
- Back stack management

## Localization

- Strings defined in `res/values/strings.xml` (English)
- Arabic translations in `res/values-ar/strings.xml`
- Other languages in respective folders
- RTL support for Arabic

## Error Handling

### Exception Strategy
1. Specific exceptions for each layer
2. Try-catch with proper logging
3. User-friendly error messages
4. Offline fallbacks where possible

### Logging
- Timber for logging
- Debug logs in development
- Error logs in production
- Remote crash reporting setup

## Configuration Management

### Build Flavors (Optional)
- Debug: Development tools enabled
- Release: Optimized, ProGuard enabled
- Beta: TestFlight/Play Console testing

### Environment Configuration
- Hardcoded URLs replaced with build variants
- Feature flags for gradual rollouts
- Dynamic configuration via API

## Documentation Standards

- KDoc for all public APIs
- Architecture Decision Records (ADR)
- Code comments for complex logic
- README for feature documentation
