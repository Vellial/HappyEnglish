package ru.lightside.relaxpaint.back.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "word_pairs")
data class WordPair(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val original: String,
    val translation: String,
    var isMatched: Boolean = false, // Угадано ли это слово
    var attempts: Int = 0, // Количество попыток
    var correctAttempts: Int = 0 // Количество правильных попыток
)