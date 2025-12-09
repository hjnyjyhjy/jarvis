# Platinum Arabic AI Assistant - Contributing Guidelines

## Welcome! 👋

Thank you for your interest in contributing to the Platinum Arabic AI Assistant project. This document provides guidelines and instructions for contributing.

## Code of Conduct

Be respectful, inclusive, and constructive in all interactions.

## How to Contribute

### 1. Report Issues
- Check if the issue already exists
- Provide clear description and steps to reproduce
- Include logs and device information
- Use issue templates when available

### 2. Suggest Features
- Describe the feature clearly
- Explain the use case and benefits
- Check if similar features exist
- Include mockups if possible

### 3. Submit Code
- Fork the repository
- Create a feature branch: `git checkout -b feature/amazing-feature`
- Follow code style guidelines
- Write tests for new features
- Commit with clear messages
- Push and create a Pull Request

## Development Setup

```bash
# Clone your fork
git clone https://github.com/your-username/platinum-assistant.git
cd platinum-assistant

# Create feature branch
git checkout -b feature/your-feature

# Install dependencies
./gradlew dependencies

# Build project
./gradlew build

# Run tests
./gradlew test
```

## Code Standards

### Kotlin Style
- Follow [Kotlin official style guide](https://kotlinlang.org/docs/coding-conventions.html)
- Use 4-space indentation
- Name classes, functions with PascalCase/camelCase
- Add KDoc for public APIs

### Architecture
- Clean Architecture with MVVM
- Separate concerns: UI, Domain, Data
- Use Dependency Injection (Hilt)
- Repository pattern for data access

### Testing
- Unit tests for logic
- Integration tests for features
- UI tests for screens
- Target > 90% coverage
- Use meaningful test names

### Documentation
- Add comments for complex logic
- Document public functions with KDoc
- Update README for new features
- Include examples where helpful

## Pull Request Process

1. **Create PR with clear title and description**
   ```
   [Feature/Bug/Docs] Short description
   
   - Detailed description
   - Changes made
   - Related issues
   ```

2. **Ensure all checks pass**
   - Code compiles without errors
   - All tests pass
   - No lint warnings
   - Code coverage maintained

3. **Wait for review**
   - Respond to feedback promptly
   - Make requested changes
   - Push updates to the same branch

4. **Merge**
   - Squash commits if requested
   - Merge to develop branch
   - Delete feature branch

## Commit Guidelines

```bash
# Format: [Type] Short description
# [Feature] Add voice recognition module
# [Bug] Fix crash on app start
# [Docs] Update contribution guidelines
# [Test] Add unit tests for encryption

git commit -m "[Feature] Add voice recognition module"
```

## Testing Requirements

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests com.platinumassistant.core.*

# Generate coverage report
./gradlew testDebugUnitTest jacocoTestDebugUnitTestReport
```

## Documentation

- Update README.md for user-facing changes
- Add/update API docs for code changes
- Include examples for new features
- Translate to Arabic if possible

## License

By contributing, you agree that your contributions will be licensed under the AGPL-3.0 License.

## Questions?

- Open a GitHub discussion
- Email: support@platinumassistant.com
- Join Telegram community

---

**Thank you for making Platinum Assistant better! 🙏**
