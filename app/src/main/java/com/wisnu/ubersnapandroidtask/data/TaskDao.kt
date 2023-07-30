package com.wisnu.ubersnapandroidtask.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

   @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(todoTask: Task)

    @Update
    suspend fun updateTask(todoTask: Task)

    @Delete
    suspend fun deleteTask(todoTask: Task)

    @Query("DELETE from tasks")
    suspend fun deleteAllTasks()

    @Query("SELECT * from tasks WHERE id = :taskId")
    fun getSelectedTask(taskId: Int): Flow<Task>

    @Query("SELECT * from tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * from tasks WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery:String): Flow<List<Task>>
}