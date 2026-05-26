package ru.lightside.happyenglish.back.repository

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.lightside.happyenglish.back.dao.PlayerDao
import ru.lightside.happyenglish.back.dao.WordDao
import ru.lightside.happyenglish.back.entity.PlayerProgress
import ru.lightside.happyenglish.back.entity.WordPair

class WordRepository @Inject constructor(
    private val wordDao: WordDao,
    private val playerDao: PlayerDao
) {
    // Получаем список слов
    fun getAllWords(): Flow<List<WordPair>> = wordDao.getAllWords()

    // Вставляем начальные данные, если база пуста
    suspend fun initializeDatabase() {
        if (wordDao.getCount() == 0) {
            val baseTime = System.currentTimeMillis() - 1000000 // Делаем системные слова "старыми"
            val initialData = listOf(
                WordPair(original = "Hello", translation = "Привет", createdAt = baseTime),
                WordPair(original = "World", translation = "Мир", createdAt = baseTime),
                WordPair(original = "Love", translation = "Любовь", createdAt = baseTime),
                WordPair(original = "Worship", translation = "Поклонение", createdAt = baseTime),
                WordPair(original = "Vain", translation = "Тщеславный", createdAt = baseTime),
                WordPair(original = "Sinful", translation = "Грешный", createdAt = baseTime),
                WordPair(original = "Confession", translation = "Признание", createdAt = baseTime),
                WordPair(original = "Obsession", translation = "Одержимость", createdAt = baseTime),
                WordPair(original = "Faith", translation = "Вера", createdAt = baseTime),
                WordPair(original = "Grace", translation = "Благодать", createdAt = baseTime),
                WordPair(original = "Mercy", translation = "Милосердие", createdAt = baseTime),
                WordPair(original = "Purity", translation = "Чистота", createdAt = baseTime),
                WordPair(original = "Sacrifice", translation = "Жертва", createdAt = baseTime),
                WordPair(original = "Wisdom", translation = "Мудрость", createdAt = baseTime),
                WordPair(original = "Courage", translation = "Мужество", createdAt = baseTime),
                WordPair(original = "Humble", translation = "Смиренный", createdAt = baseTime),
                WordPair(original = "Patience", translation = "Терпение", createdAt = baseTime),
                WordPair(original = "Gentle", translation = "Нежный", createdAt = baseTime),
                WordPair(original = "Kindness", translation = "Доброта", createdAt = baseTime),
                WordPair(original = "Justice", translation = "Справедливость", createdAt = baseTime),
                WordPair(original = "Liberty", translation = "Свобода", createdAt = baseTime),
                WordPair(original = "Honesty", translation = "Честность", createdAt = baseTime),
                WordPair(original = "Unity", translation = "Единство", createdAt = baseTime),
                WordPair(original = "Harmony", translation = "Гармония", createdAt = baseTime),
                WordPair(original = "Victory", translation = "Победа", createdAt = baseTime)
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

    suspend fun getPlayerProgress(): PlayerProgress {
        return playerDao.getPlayerProgress() ?: PlayerProgress()
    }

    suspend fun savePlayerProgress(progress: PlayerProgress) {
        playerDao.savePlayerProgress(progress)
    }
}
