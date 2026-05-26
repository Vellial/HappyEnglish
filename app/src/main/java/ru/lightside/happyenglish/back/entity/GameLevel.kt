package ru.lightside.happyenglish.back.entity

data class GameLevel(
    val id: Int,
    val name: String,
    val wordCount: Int,
    val timeLimitSeconds: Int?,  // null = без ограничений по времени
    val requiredAccuracy: Float, // минимальная точность (0.0 - 1.0)
    val coinsReward: Int,
    val xpReward: Int
)
