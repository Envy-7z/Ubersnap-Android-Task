package com.wisnu.ubersnapandroidtask.di

import android.content.Context
import androidx.room.Room
import com.wisnu.ubersnapandroidtask.data.TaskDao
import com.wisnu.ubersnapandroidtask.data.TaskDatabase
import com.wisnu.ubersnapandroidtask.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TaskDatabase = Room.databaseBuilder(
        context,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideDao(database: TaskDatabase): TaskDao = database.taskDao()

}