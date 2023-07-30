package com.wisnu.ubersnapandroidtask.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.wisnu.ubersnapandroidtask.data.Task
import com.wisnu.ubersnapandroidtask.ui.viewmodels.TaskViewModel
import com.wisnu.ubersnapandroidtask.utils.Action

@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel,
    selectedTask: Task?,
    navigateToListScreen: (Action) -> Unit
) {
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TaskAppBar(
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (taskViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }
                },
                selectedTask = selectedTask
            )
        }
    ) {
        TaskContent(
            title = taskViewModel.title.value,
            onTitleChange = taskViewModel::onTitleChange,
            description = taskViewModel.description.value,
            onDescriptionChange = taskViewModel::onDescriptionChange,
            dueDate = taskViewModel.dueDate.value,
            onDueDateChange = taskViewModel::onDuedateChange
        )
        it
    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields Empty",
        Toast.LENGTH_SHORT
    ).show()
}
