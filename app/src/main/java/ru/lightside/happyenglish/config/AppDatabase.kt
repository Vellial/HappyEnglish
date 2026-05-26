package ru.lightside.happyenglish.config

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lightside.happyenglish.back.dao.PlayerDao
import ru.lightside.happyenglish.back.dao.WordDao
import ru.lightside.happyenglish.back.entity.PlayerProgress
import ru.lightside.happyenglish.back.entity.WordPair

@Database(entities = [WordPair::class, PlayerProgress::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun playerDao(): PlayerDao
}
