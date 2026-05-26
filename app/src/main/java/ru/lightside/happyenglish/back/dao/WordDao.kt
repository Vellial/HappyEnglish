package ru.lightside.happyenglish.back.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.lightside.happyenglish.back.entity.WordPair

@Dao
interface WordDao {
    @Query("SELECT * FROM word_pairs ORDER BY createdAt DESC")
    fun getAllWords(): Flow<List<WordPair>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordPair)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<WordPair>)

    @Query("SELECT COUNT(*) FROM word_pairs")
    suspend fun getCount(): Int

    @Delete
    suspend fun deleteWord(word: WordPair)

    @Query("UPDATE word_pairs SET attempts = attempts + 1, correctAttempts = correctAttempts + :isCorrect WHERE id = :id")
    suspend fun updateAttempts(id: String, isCorrect: Boolean)

    @Query("SELECT * FROM word_pairs WHERE attempts > 0 ORDER BY correctAttempts ASC, attempts DESC LIMIT :limit")
    suspend fun getWordsForReview(limit: Int): List<WordPair>

    @Query("UPDATE word_pairs SET isMatched = 1 WHERE id = :id")
    suspend fun markWordAsMatched(id: String)

    @Query("UPDATE word_pairs SET isMatched = 0")
    suspend fun resetAllWords()
}
