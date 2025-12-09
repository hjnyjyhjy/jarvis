# Feature Implementation Guide

## Overview

This document provides detailed guidance on implementing features in the Platinum Arabic AI Assistant project.

## Feature Categories & Implementation

### 1. 🎭 Personalities (50+ Characters)

#### Structure
```
features/personalities/
├── PersonalityEntity.kt
├── PersonalityRepository.kt
├── PersonalityUseCases.kt
├── PersonalityViewModel.kt
├── PersonalitiesScreen.kt
└── components/
    ├── PersonalityCard.kt
    └── PersonalitySelector.kt
```

#### Implementation Steps

**Step 1: Define Personality Data**
```kotlin
data class Personality(
    val id: String,
    val name: String,
    val voiceId: String,
    val category: PersonalityCategory,
    val characteristics: List<String>,
    // ... other fields
)
```

**Step 2: Create Database Entity**
```kotlin
@Entity(tableName = "personalities")
data class PersonalityEntity(
    @PrimaryKey val id: String,
    val name: String,
    // ... fields mapped to database
)
```

**Step 3: Create DAO (Data Access Object)**
```kotlin
@Dao
interface PersonalityDao {
    @Query("SELECT * FROM personalities")
    fun getAllPersonalities(): Flow<List<PersonalityEntity>>
    
    @Insert
    suspend fun insertPersonality(personality: PersonalityEntity)
    // ... other queries
}
```

**Step 4: Implement Repository**
```kotlin
class PersonalityRepositoryImpl(
    private val personalityDao: PersonalityDao
) : PersonalityRepository {
    override fun getAllPersonalities(): Flow<List<Personality>> {
        return personalityDao.getAllPersonalities()
            .map { entities -> entities.map { it.toDomain() } }
    }
    // ... other methods
}
```

**Step 5: Create Use Cases**
```kotlin
class GetAllPersonalitiesUseCase(
    private val repository: PersonalityRepository
) {
    operator fun invoke(): Flow<List<Personality>> {
        return repository.getAllPersonalities()
    }
}
```

**Step 6: Create ViewModel**
```kotlin
@HiltViewModel
class PersonalitiesViewModel @Inject constructor(
    private val getAllPersonalitiesUseCase: GetAllPersonalitiesUseCase
) : ViewModel() {
    val personalities = getAllPersonalitiesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
```

**Step 7: Create UI Screen**
```kotlin
@Composable
fun PersonalitiesScreen(
    viewModel: PersonalitiesViewModel = hiltViewModel()
) {
    val personalities by viewModel.personalities.collectAsState()
    
    LazyColumn {
        items(personalities) { personality ->
            PersonalityCard(personality)
        }
    }
}
```

#### Voice Integration
```kotlin
class VoicePersonality(
    val personality: Personality,
    val voiceProvider: VoiceProvider // Piper TTS
) {
    suspend fun speak(text: String) {
        voiceProvider.synthesize(
            text = text,
            voiceId = personality.voiceId,
            speed = personality.speed,
            pitch = personality.pitch
        )
    }
}
```

### 2. 👤 Personal Assistant Features

#### Tasks Management
```
features/assistant/
├── tasks/
│   ├── Task.kt
│   ├── TaskRepository.kt
│   ├── TaskViewModel.kt
│   ├── TasksScreen.kt
│   └── components/
│       ├── TaskItem.kt
│       └── TaskForm.kt
```

#### Implementation
```kotlin
// Add Task Use Case
class AddTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        require(task.title.isNotEmpty()) { "Title cannot be empty" }
        taskRepository.addTask(task)
    }
}

// ViewModel with validation
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {
    fun addTask(title: String, dueDate: Long?) {
        viewModelScope.launch {
            try {
                val task = Task(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    dueDate = dueDate
                )
                addTaskUseCase(task)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
```

#### Voice Input for Tasks
```kotlin
class VoiceTaskInput(
    private val speechRecognizer: SpeechRecognizer,
    private val nlpProcessor: NLPProcessor
) {
    suspend fun createTaskFromVoice(): Task {
        val text = speechRecognizer.listen() // "Add meeting tomorrow at 10"
        val extracted = nlpProcessor.extractTaskInfo(text)
        
        return Task(
            title = extracted.title,
            dueDate = extracted.dueDate,
            priority = extracted.priority
        )
    }
}
```

### 3. 👨‍💻 Programmer Assistant

#### Code Generation
```kotlin
class CodeGenerationService(
    private val llmModel: LocalLLM
) {
    suspend fun generateCode(
        language: String,
        description: String,
        personality: Personality
    ): String {
        val prompt = """
            You are a ${personality.name} programmer assistant.
            Generate $language code for: $description
            Include comments explaining the code.
        """.trimIndent()
        
        return llmModel.generate(prompt)
    }
}
```

#### Git Integration
```kotlin
class GitCommandExecutor(
    private val repository: File
) {
    fun executeCommand(command: String): String {
        return ProcessBuilder("git", "-C", repository.path, *command.split(" ").toTypedArray())
            .redirectErrorStream(true)
            .start()
            .inputStream
            .bufferedReader()
            .readText()
    }
    
    suspend fun commitChanges(message: String) {
        executeCommand("add .")
        executeCommand("commit -m '$message'")
    }
}
```

### 4. 💪 Health & Wellness Features

#### Health Tracking
```kotlin
data class HealthMetrics(
    val waterIntake: Int, // ml
    val sleepHours: Float,
    val exerciseMinutes: Int,
    val heartRate: Int? = null,
    val steps: Int? = null
)

class HealthTrackingService(
    private val repository: HealthMetricsRepository
) {
    suspend fun logWaterIntake(amount: Int) {
        val metrics = repository.getTodayMetrics()
        repository.saveTodayMetrics(
            metrics.copy(waterIntake = metrics.waterIntake + amount)
        )
    }
}
```

#### Health Recommendations
```kotlin
class HealthRecommendationEngine(
    private val metrics: HealthMetrics,
    private val userProfile: UserProfile
) {
    fun generateRecommendations(): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (metrics.waterIntake < 2000) {
            recommendations.add("اشرب المزيد من الماء!")
        }
        
        if (metrics.sleepHours < 7) {
            recommendations.add("حاول النوم أكثر للراحة الكافية")
        }
        
        if (metrics.exerciseMinutes < 30) {
            recommendations.add("مارس تمارين خفيفة اليوم")
        }
        
        return recommendations
    }
}
```

### 5. 🎮 Entertainment Features

#### Voice Games
```kotlin
interface VoiceGame {
    suspend fun startGame(): GameSession
    suspend fun processPlayerInput(input: String): GameResult
    suspend fun endGame(): GameScore
}

class TriviaGame(
    private val speechRecognizer: SpeechRecognizer,
    private val textToSpeech: TextToSpeech
) : VoiceGame {
    override suspend fun startGame(): GameSession {
        textToSpeech.speak("مرحبا بك في لعبة الثقافة العامة!")
        // Load questions and start game loop
        return GameSession(/* ... */)
    }
}
```

#### Interactive Stories
```kotlin
data class StoryNode(
    val id: String,
    val text: String,
    val choices: List<Choice>
)

data class Choice(
    val id: String,
    val text: String,
    val nextNodeId: String
)

class InteractiveStory(
    private val nodes: Map<String, StoryNode>,
    private val textToSpeech: TextToSpeech
) {
    var currentNodeId = "start"
    
    suspend fun readCurrentNode() {
        val node = nodes[currentNodeId] ?: return
        textToSpeech.speak(node.text)
    }
    
    suspend fun makeChoice(choiceId: String) {
        val node = nodes[currentNodeId] ?: return
        val choice = node.choices.find { it.id == choiceId } ?: return
        currentNodeId = choice.nextNodeId
        readCurrentNode()
    }
}
```

## Testing Feature Implementation

### Unit Testing Template
```kotlin
class FeatureUseCaseTest {
    
    private lateinit var mockRepository: FeatureRepository
    private lateinit var useCase: FeatureUseCase
    
    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = FeatureUseCase(mockRepository)
    }
    
    @Test
    fun testFeatureBehavior() = runTest {
        // Arrange
        val expectedResult = "expected value"
        coEvery { mockRepository.fetchData() } returns expectedResult
        
        // Act
        val result = useCase()
        
        // Assert
        assertEquals(expectedResult, result)
    }
}
```

### Integration Testing Template
```kotlin
@RunWith(AndroidJUnit4::class)
class FeatureIntegrationTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testFeatureScreenRendering() {
        composeTestRule.setContent {
            FeatureScreen()
        }
        
        composeTestRule.onNodeWithText("Expected Text")
            .assertIsDisplayed()
    }
}
```

## Localization for Features

### Add Strings for Feature
```xml
<!-- res/values/strings.xml -->
<string name="task_title">المهام</string>
<string name="task_description">إدارة المهام اليومية</string>

<!-- res/values-ar/strings.xml -->
<string name="task_title">المهام</string>
<string name="task_description">إدارة المهام اليومية</string>
```

### Dynamic Localization
```kotlin
fun getLocalizedString(
    context: Context,
    stringId: Int,
    language: String
): String {
    val config = Configuration(context.resources.configuration)
    config.locale = Locale(language)
    val localizedContext = context.createConfigurationContext(config)
    return localizedContext.getString(stringId)
}
```

## Performance Considerations

### Memory-Efficient Lists
```kotlin
@Composable
fun LargeListFeature(items: List<Item>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items.size,
            key = { items[it].id } // Optimize recomposition
        ) { index ->
            ItemCard(items[index])
        }
    }
}
```

### Database Optimization
```kotlin
@Query("SELECT * FROM items WHERE category = :category ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
fun getItemsByCategory(
    category: String,
    limit: Int = 20,
    offset: Int = 0
): Flow<List<ItemEntity>>
```

## State Management Pattern

```kotlin
data class FeatureUiState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val repository: FeatureRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeatureUiState())
    val uiState: StateFlow<FeatureUiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val data = repository.getData()
                _uiState.update { it.copy(data = data, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
```

## Adding New Features Checklist

- [ ] Create feature package in `features/`
- [ ] Define domain entities
- [ ] Create database entities and DAOs
- [ ] Implement repository interface
- [ ] Implement repository class
- [ ] Create use cases
- [ ] Create ViewModel with state management
- [ ] Create UI screens and components
- [ ] Add feature tests (unit + integration)
- [ ] Add localization strings
- [ ] Update ROADMAP.md
- [ ] Update feature documentation
- [ ] Create PR with clear description

## Common Patterns

### Error Handling
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```

### Pagination
```kotlin
class PaginationViewModel(
    private val repository: Repository
) : ViewModel() {
    private var currentPage = 0
    
    fun loadMore() {
        viewModelScope.launch {
            val data = repository.getPagedData(currentPage++)
            // Update UI with new data
        }
    }
}
```

---

**For more information, see:**
- ARCHITECTURE.md - Overall architecture
- ROADMAP.md - Development timeline
- CONTRIBUTING.md - Contribution guidelines
