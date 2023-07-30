package com.wisnu.ubersnapandroidtask.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisnu.ubersnapandroidtask.utils.RequestState
import com.wisnu.ubersnapandroidtask.data.Task
import com.wisnu.ubersnapandroidtask.data.TaskRepository
import com.wisnu.ubersnapandroidtask.utils.Action
import com.wisnu.ubersnapandroidtask.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
) : ViewModel(){

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    fun changeAction(newAction: Action) {
        action.value = newAction
    }

    private val _allTasks =
        MutableStateFlow<RequestState<List<Task>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<Task>>> = _allTasks

    // states
    val id: MutableState<Int> = mutableStateOf(0)

    val title: MutableState<String> = mutableStateOf("")

    val description: MutableState<String> = mutableStateOf("")

    val dueDate: MutableState<String> = mutableStateOf("")

    init {
        getAllTasks()
    }


    private val _searchedTasks =
        MutableStateFlow<RequestState<List<Task>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<Task>>> = _searchedTasks

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                // Finds any values that have searchQuery in any position - sql LIKE
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                    }
            }

        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)

        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    // state: searchAppBarState
    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
        private set //! I do not understand !!!!!

    // state: searchTextState
    var searchTextState: MutableState<String> = mutableStateOf("")
        private set

    // onSearchClicked is an event we're defining that the UI can invoke
    // (events flow up from UI)
    // event: onSearchClicked
    fun onSearchClicked(newSearchAppBarState: SearchAppBarState) {
        searchAppBarState.value = newSearchAppBarState
    }

    // event: onSearchTextChanged
    fun onSearchTextChanged(newText: String) {
        searchTextState.value = newText
    }


    // events:

    fun onTitleChange(newTitle: String) {
        // limit the character count
        if (newTitle.length < 20) {
            title.value = newTitle
        }
    }

    fun onDescriptionChange(newDescription: String) {
        description.value = newDescription
    }

    fun onDuedateChange (newDueDate: String) {
        dueDate.value = newDueDate
    }

    fun updateTaskFields(selectedTask: Task?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            dueDate.value + selectedTask.dueDateMillis
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            dueDate.value = ""
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty() && dueDate.value.isNotEmpty()
    }


    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                // Trigger the flow and consume its elements using collect
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)

        }
    }


    private val _selectedTask: MutableStateFlow<Task?> = MutableStateFlow(null)
    val selectTask: StateFlow<Task?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect {
                _selectedTask.value = it
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = Task(
                title = title.value,
                description = description.value,
                dueDateMillis = dueDate.value
            )
            repository.addTask(todoTask = todoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = Task(
                id = id.value,
                title = title.value,
                description = description.value,
                dueDateMillis = dueDate.value
            )
            repository.updateTask(todoTask = todoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = Task(
                id = id.value,
                title = title.value,
                description = description.value,
                dueDateMillis = dueDate.value
            )
            repository.deleteTask(todoTask = todoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
            Action.DELETE_ALL -> deleteAllTasks()
            Action.UNDO -> addTask()
            else -> {
            }
        }
        this.action.value = Action.NO_ACTION
    }
}