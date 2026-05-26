package ru.lightside.happyenglish.back.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.lightside.happyenglish.back.dao.PlayerDao
import ru.lightside.happyenglish.back.dao.WordDao
import ru.lightside.happyenglish.config.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_dictionary_db"
        ).fallbackToDestructiveMigration(dropAllTables = true)
        .build()
    }

    @Provides
    fun provideWordDao(database: AppDatabase): WordDao {
        return database.wordDao()
    }

    @Provides
    fun providePlayerDao(database: AppDatabase): PlayerDao {
        return database.playerDao()
    }
}
