package ru.lightside.happyenglish.back.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.lightside.happyenglish.back.entity.PlayerProgress

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_progress WHERE id = 1")
    suspend fun getPlayerProgress(): PlayerProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayerProgress(progress: PlayerProgress)
}
