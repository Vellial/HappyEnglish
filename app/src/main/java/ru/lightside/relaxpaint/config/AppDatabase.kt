package ru.lightside.relaxpaint.config

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lightside.relaxpaint.back.dao.WordDao
import ru.lightside.relaxpaint.back.entity.WordPair

@Database(entities = [WordPair::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
