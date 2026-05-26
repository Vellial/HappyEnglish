package ru.lightside.relaxpaint.viewmodels

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
import ru.lightside.relaxpaint.back.entity.WordPair
import ru.lightside.relaxpaint.back.repository.WordRepository

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    // Добавляем состояние для угаданных пар
    var matchedIds by mutableStateOf<Set<String>>(emptySet())
        private set

    var errorIds by mutableStateOf<Set<String>>(emptySet())
    private var errorResetJob: Job? = null

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

    init {
        loadGameData()
        viewModelScope.launch {
            wordPairs.collect { pairs ->
                if (pairs.isNotEmpty() && _shuffledOriginals.value.isEmpty()) {
                    _shuffledOriginals.value = pairs.shuffled()
                    _shuffledTranslations.value = pairs.shuffled()
                }
            }
        }
    }

    // Функция проверки
    fun onSelectionChanged(id: String, isOriginal: Boolean) {
        // Если слово уже угадано, ничего не делаем
        if (matchedIds.contains(id)) return

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
            // Получаем ID, которые сейчас выбраны
            val original = selectedOriginalId!!
            val translation = selectedTranslationId!!

            // ПРОВЕРКА: совпадают ли они по логике (ID должны быть одинаковыми, так как они из одного WordPair)
            if (original == translation) {
                markAsMatched(original)
                updateAttempts(original, true)
            } else {
                // Ошибка: сохраняем оба ID для подсветки
                errorIds = setOf(original, translation)

                updateAttempts(original, false)
                updateAttempts(translation, false)

                errorResetJob?.cancel()
                errorResetJob = viewModelScope.launch {
                    delay(500)
                    errorIds = emptySet()
                }
            }

            // Сбрасываем выбор
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

    private fun markAsMatched(id: String) {
        // Добавляем ID в список успешно угаданных
        matchedIds = matchedIds + id
    }
}