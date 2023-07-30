package com.wisnu.ubersnapandroidtask.ui.screens.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.wisnu.ubersnapandroidtask.R
import com.wisnu.ubersnapandroidtask.ui.theme.fabBackgroundColor
import com.wisnu.ubersnapandroidtask.ui.viewmodels.TaskViewModel
import com.wisnu.ubersnapandroidtask.utils.Action
import com.wisnu.ubersnapandroidtask.utils.SearchAppBarState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    taskViewModel: TaskViewModel,
    action: Action
) {
    
    val allTasks by taskViewModel.allTasks.collectAsState() // the type is  RequestState<List<TodoTask>>

    val searchAppBarState: SearchAppBarState by taskViewModel.searchAppBarState

    val searchTextState: String = taskViewModel.searchTextState.value

    val searchedTasks by taskViewModel.searchedTasks.collectAsState()



    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // every time the ListScreen function triggers the following function is triggered
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { taskViewModel.handleDatabaseActions(action) },
        taskTitle = taskViewModel.title.value,
        action = action,
        onUndoClicked = {
            taskViewModel.action.value = it
        },
    )


    Scaffold(
        // to be able to display the snackbar
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                taskViewModel = taskViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                onSearchClicked = taskViewModel::onSearchClicked,
                onTextChange = taskViewModel::onSearchTextChanged
            )
        },
        floatingActionButton = {
            ListFab(navigateToTaskScreen = navigateToTaskScreen)
        }
    ) {
        ListContent(
            navigateToTaskScreen = navigateToTaskScreen,
            allTasks = allTasks,
            searchAppBarState = searchAppBarState,
            searchedTasks = searchedTasks
        )
        it
    }
}

@Composable
fun ListFab(
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = { navigateToTaskScreen(-1) },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    action: Action,
    onUndoClicked: (Action) -> Unit
) {
    //! May be improved. For now this function is triggered any time there is a
    //! recomposition of DisplaySnackBar composable. Ideally, it should be triggered
    //! when there is a change in action by means of a LaunchedEffect with key1 = action
    handleDatabaseActions()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackbarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setMessage(
    action: Action,
    taskTitle: String
): String {

    return when (action) {
        Action.DELETE_ALL -> "All tasks removed."
        else -> "${action.name}: $taskTitle"
    }

}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}

