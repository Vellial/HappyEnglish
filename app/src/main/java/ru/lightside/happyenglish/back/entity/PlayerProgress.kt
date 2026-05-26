package ru.lightside.happyenglish.back.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_progress")
data class PlayerProgress(
    @PrimaryKey val id: Int = 1, // Один игрок
    val currentLevel: Int = 1,
    val xp: Int = 0,
    val coins: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalAttempts: Int = 0
)
