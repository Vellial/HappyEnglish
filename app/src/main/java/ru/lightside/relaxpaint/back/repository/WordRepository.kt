package ru.lightside.relaxpaint.back.repository

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.lightside.relaxpaint.back.dao.WordDao
import ru.lightside.relaxpaint.back.entity.WordPair

class WordRepository @Inject constructor(private val wordDao: WordDao) {
    // Получаем список слов
    fun getAllWords(): Flow<List<WordPair>> = wordDao.getAllWords()

    // Вставляем начальные данные, если база пуста
    suspend fun initializeDatabase() {
        if (wordDao.getCount() == 0) {
            val initialData = listOf(
                WordPair(original = "Hello", translation = "Привет"),
                WordPair(original = "World", translation = "Мир"),
                WordPair(original = "Love", translation = "Любовь"),
                WordPair(original = "Worship", translation = "Поклонение"),
                WordPair(original = "Vain", translation = "Тщеславный"),
                WordPair(original = "Sinful", translation = "Грешный"),
                WordPair(original = "Confession", translation = "Признание"),
                WordPair(original = "Obsession", translation = "Одержимость")
            )
            wordDao.insertAll(initialData)
        }
    }

    suspend fun updateAttempts(id: String, correct: Boolean) {
        wordDao.updateAttempts(id, correct)
    }

    suspend fun getWordsForReview(limit: Int): List<WordPair> {
        return wordDao.getWordsForReview(limit)
    }
}