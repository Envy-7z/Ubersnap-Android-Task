package com.wisnu.ubersnapandroidtask.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import com.wisnu.ubersnapandroidtask.utils.FilterUtils
import com.wisnu.ubersnapandroidtask.utils.TasksFilterType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TaskRepository @Inject constructor(private val tasks: TaskDao) {

    val getAllTasks: Flow<List<Task>> = tasks.getAllTasks()


    private fun getFilteredQuery(filter: TasksFilterType): SimpleSQLiteQuery {
        return FilterUtils.getFilteredQuery(filter)
    }
    fun getSelectedTask(taskId: Int): Flow<Task> {
        return tasks.getSelectedTask(taskId = taskId)
    }

    suspend fun addTask(todoTask: Task) {
        tasks.addTask(todoTask = todoTask)
    }

    suspend fun updateTask(todoTask: Task) {
        tasks.updateTask(todoTask = todoTask)
    }

    suspend fun deleteTask(todoTask: Task) {
        tasks.deleteTask(todoTask = todoTask)
    }

    suspend fun deleteAllTasks() {
        tasks.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String): Flow<List<Task>> {
        return tasks.searchDatabase(searchQuery = searchQuery)
    }
    
    
}