package ru.lightside.happyenglish.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.lightside.happyenglish.back.dao.WordDao
import ru.lightside.happyenglish.back.entity.WordPair
import ru.lightside.happyenglish.event.UiEvent

@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val wordDao: WordDao
) : ViewModel() {

    var original by mutableStateOf("")
        private set
    var translation by mutableStateOf("")
        private set

    val hasChanges: Boolean
        get() = original.isNotBlank() || translation.isNotBlank()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val isFormValid: Boolean
        get() = original.isNotBlank() && translation.isNotBlank()

    fun onOriginalChange(newTitle: String) {
        original = newTitle
    }

    fun onTranslationChange(newDesc: String) {
        translation = newDesc
    }

    fun saveWord() {
        if (!isFormValid) return
        viewModelScope.launch {
            try {
                val newWord = WordPair(original = original, translation = translation)
                wordDao.insertWord(newWord)

                _uiEvent.send(UiEvent.NavigateBack)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.ShowSnackbar("Ошибка при сохранении: ${e.localizedMessage}"))
            }

        }
    }
}
