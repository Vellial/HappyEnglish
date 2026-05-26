package ru.lightside.happyenglish.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lightside.happyenglish.back.entity.GameLevel
import ru.lightside.happyenglish.back.entity.PlayerProgress
import ru.lightside.happyenglish.back.entity.WordPair
import ru.lightside.happyenglish.back.repository.WordRepository
import ru.lightside.happyenglish.data.GameLevels

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    companion object {
        private const val MAX_VISIBLE_PAIRS = 6
    }

    // Добавляем состояние для угаданных пар
    var matchedIds by mutableStateOf<Set<String>>(emptySet())
        private set

    var errorIds by mutableStateOf<Set<String>>(emptySet())
    private var errorResetJob: Job? = null
    private var timerJob: Job? = null

    // Состояние выбора
    var selectedOriginalId by mutableStateOf<String?>(null)
    var selectedTranslationId by mutableStateOf<String?>(null)

    val wordPairs: StateFlow<List<WordPair>> = repository.getAllWords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _shuffledOriginals = MutableStateFlow<List<WordPair>>(emptyList())
    val shuffledOriginals = _shuffledOriginals.asStateFlow()

    private val _shuffledTranslations = MutableStateFlow<List<WordPair>>(emptyList())
    val shuffledTranslations = _shuffledTranslations.asStateFlow()

    // Пул оставшихся слов уровня
    private var levelPool = mutableListOf<WordPair>()

    // Новые поля для уровней и наград
    private val _currentLevel = MutableStateFlow(1)
    val currentLevel = _currentLevel.asStateFlow()

    private val _playerProgress = MutableStateFlow(PlayerProgress())
    val playerProgress = _playerProgress.asStateFlow()

    private val _gameState = MutableStateFlow(GameState.Idle)
    val gameState = _gameState.asStateFlow()

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()

    // Получаем текущий уровень
    val currentLevelData: GameLevel
        get() = GameLevels.levels.getOrElse(_currentLevel.value - 1) { GameLevels.levels[0] }

    private var sessionCorrectAnswers = 0
    private var sessionTotalAttempts = 0
    private var totalPairsInLevel = 0

    init {
        loadGameData()
        loadPlayerProgress()
        viewModelScope.launch {
            wordPairs.collect { pairs ->
                if (pairs.isNotEmpty() && _gameState.value == GameState.Idle) {
                    startLevel(pairs)
                }
            }
        }
    }

    private fun startLevel(allPairs: List<WordPair>) {
        val count = currentLevelData.wordCount
        val levelPairs = allPairs.take(count)
        totalPairsInLevel = levelPairs.size
        
        levelPool = levelPairs.toMutableList()
        
        // Берем первые 6 пар для отображения
        val initialVisible = levelPool.take(MAX_VISIBLE_PAIRS)
        levelPool.removeAll(initialVisible)

        _shuffledOriginals.value = initialVisible.shuffled()
        _shuffledTranslations.value = initialVisible.shuffled()
        
        matchedIds = emptySet()
        sessionCorrectAnswers = 0
        sessionTotalAttempts = 0
        _gameState.value = GameState.Playing
        
        startTimer()
    }

    private fun refillActiveWords() {
        if (levelPool.isEmpty()) return
        
        viewModelScope.launch {
            delay(300) // Задержка для красоты анимации исчезновения
            
            val nextPair = levelPool.removeAt(0)
            
            // Добавляем новое слово в случайное место, чтобы было интереснее
            val currentOriginals = _shuffledOriginals.value.toMutableList()
            val currentTranslations = _shuffledTranslations.value.toMutableList()
            
            currentOriginals.add((0..currentOriginals.size).random(), nextPair)
            currentTranslations.add((0..currentTranslations.size).random(), nextPair)
            
            _shuffledOriginals.value = currentOriginals
            _shuffledTranslations.value = currentTranslations
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        val limit = currentLevelData.timeLimitSeconds
        if (limit != null) {
            _timer.value = limit
            timerJob = viewModelScope.launch {
                while (_timer.value > 0 && _gameState.value == GameState.Playing) {
                    delay(1000)
                    _timer.value -= 1
                }
                if (_timer.value <= 0 && _gameState.value == GameState.Playing) {
                    onLevelFailed()
                }
            }
        } else {
            _timer.value = 0
        }
    }

    private fun onLevelFailed() {
        _gameState.value = GameState.LevelFailed
    }

    private fun onLevelCompleted() {
        timerJob?.cancel()
        _gameState.value = GameState.LevelCompleted
        
        val accuracy = if (sessionTotalAttempts > 0) {
            sessionCorrectAnswers.toFloat() / sessionTotalAttempts
        } else 1f
        
        if (accuracy >= currentLevelData.requiredAccuracy) {
            applyRewards()
        } else {
            _gameState.value = GameState.LevelFailed
        }
    }

    private fun applyRewards() {
        val currentProgress = _playerProgress.value
        val newProgress = currentProgress.copy(
            xp = currentProgress.xp + currentLevelData.xpReward,
            coins = currentProgress.coins + currentLevelData.coinsReward,
            currentLevel = if (_currentLevel.value == currentProgress.currentLevel) _currentLevel.value + 1 else currentProgress.currentLevel,
            totalCorrectAnswers = currentProgress.totalCorrectAnswers + sessionCorrectAnswers,
            totalAttempts = currentProgress.totalAttempts + sessionTotalAttempts
        )
        _playerProgress.value = newProgress
        savePlayerProgress()
    }

    // Загружаем прогресс игрока
    private fun loadPlayerProgress() {
        viewModelScope.launch {
            val progress = repository.getPlayerProgress()
            _playerProgress.value = progress
            _currentLevel.value = progress.currentLevel
        }
    }

    // Сохраняем прогресс игрока
    private fun savePlayerProgress() {
        viewModelScope.launch {
            repository.savePlayerProgress(_playerProgress.value)
        }
    }

    // Функция проверки
    fun onSelectionChanged(id: String, isOriginal: Boolean) {
        if (_gameState.value != GameState.Playing) return
        
        // 1. Если мы уже нажали на этот же элемент, игнорируем или отменяем выбор
        if (isOriginal && selectedOriginalId == id) {
            selectedOriginalId = null
            return
        }
        if (!isOriginal && selectedTranslationId == id) {
            selectedTranslationId = null
            return
        }

        // 2. Установка выбора
        if (isOriginal) {
            selectedOriginalId = id
        } else {
            selectedTranslationId = id
        }

        // 3. Проверка
        if (selectedOriginalId != null && selectedTranslationId != null) {
            val original = selectedOriginalId!!
            val translation = selectedTranslationId!!

            sessionTotalAttempts++
            
            if (original == translation) {
                sessionCorrectAnswers++
                markAsMatched(original)
                updateAttempts(original, true)
                
                // Удаляем угаданное из активных списков
                _shuffledOriginals.value = _shuffledOriginals.value.filter { it.id != original }
                _shuffledTranslations.value = _shuffledTranslations.value.filter { it.id != translation }
                
                // Пополняем новыми словами, если есть
                refillActiveWords()
                
                if (matchedIds.size == totalPairsInLevel) {
                    onLevelCompleted()
                }
            } else {
                errorIds = setOf(original, translation)
                updateAttempts(original, false)
                updateAttempts(translation, false)

                errorResetJob?.cancel()
                errorResetJob = viewModelScope.launch {
                    delay(500)
                    errorIds = emptySet()
                }
            }

            selectedOriginalId = null
            selectedTranslationId = null
        }
    }

    private fun loadWordsForReview() {
        viewModelScope.launch {
            val wordsForReview = repository.getWordsForReview(10)
            if (wordsForReview.isNotEmpty()) {
                _shuffledOriginals.value = wordsForReview.shuffled()
                _shuffledTranslations.value = wordsForReview.shuffled()
            } else {
                // Все слова повторены — можно предложить добавить новые
            }
        }
    }

    private fun updateAttempts(id: String, isCorrect: Boolean) {
        viewModelScope.launch {
            repository.updateAttempts(id, isCorrect)
        }
    }

    private fun loadGameData() {
        // Запуск инициализации при создании ViewModel
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    fun continueGame() {
        if (_gameState.value == GameState.LevelCompleted) {
            _currentLevel.value += 1
        }
        val pairs = wordPairs.value
        if (pairs.isNotEmpty()) {
            startLevel(pairs)
        }
    }

    private fun markAsMatched(id: String) {
        // Добавляем ID в список успешно угаданных
        matchedIds = matchedIds + id
    }
}