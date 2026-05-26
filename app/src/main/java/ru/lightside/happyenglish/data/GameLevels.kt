package ru.lightside.happyenglish.data

import ru.lightside.happyenglish.back.entity.GameLevel

object GameLevels {
    val levels = listOf(
        GameLevel(
            id = 1,
            name = "Начинающий",
            wordCount = 5,
            timeLimitSeconds = null,
            requiredAccuracy = 0.0f,
            coinsReward = 10,
            xpReward = 50
        ),
        GameLevel(
            id = 2,
            name = "Продвинутый",
            wordCount = 10,
            timeLimitSeconds = null,
            requiredAccuracy = 0.5f,
            coinsReward = 20,
            xpReward = 100
        ),
        GameLevel(
            id = 3,
            name = "Эксперт",
            wordCount = 15,
            timeLimitSeconds = 60,
            requiredAccuracy = 0.7f,
            coinsReward = 50,
            xpReward = 200
        ),
        GameLevel(
            id = 4,
            name = "Мастер",
            wordCount = 20,
            timeLimitSeconds = 60,
            requiredAccuracy = 0.8f,
            coinsReward = 100,
            xpReward = 500
        ),
        GameLevel(
            id = 5,
            name = "Легенда",
            wordCount = 25,
            timeLimitSeconds = 45,
            requiredAccuracy = 0.9f,
            coinsReward = 200,
            xpReward = 1000
        )
    )
}
