package com.wisnu.ubersnapandroidtask.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.wisnu.ubersnapandroidtask.ui.theme.LARGE_PADDING
import com.wisnu.ubersnapandroidtask.ui.theme.TASK_ITEM_ELEVATION
import com.wisnu.ubersnapandroidtask.data.Task
import com.wisnu.ubersnapandroidtask.ui.theme.taskItemBackgroundColor
import com.wisnu.ubersnapandroidtask.ui.theme.taskItemTextColor
import com.wisnu.ubersnapandroidtask.utils.RequestState
import com.wisnu.ubersnapandroidtask.utils.SearchAppBarState

@ExperimentalMaterialApi
@Composable
fun ListContent(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    allTasks: RequestState<List<Task>>,
    searchAppBarState: SearchAppBarState,
    searchedTasks: RequestState<List<Task>>
) {
    if (allTasks is RequestState.Success) {
        when (searchAppBarState) {
            SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }

            else -> {
                HandleListContent(
                    tasks = allTasks.data,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    tasks: List<Task>,
    navigateToTaskScreen: (taskId: Int) -> Unit

) {
    if (tasks.isEmpty()) EmptyContent() else
        DisplayTasks(
            tasks = tasks,
            navigateToTaskScreen = navigateToTaskScreen
        )

}

@ExperimentalMaterialApi
@Composable
fun DisplayTasks(tasks: List<Task>, navigateToTaskScreen: (taskId: Int) -> Unit) {
    LazyColumn {
        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            TaskItem(
                todoTask = task,
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun TaskItem(
    todoTask: Task,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(todoTask.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        )
        {
            Row {
                Text(
                    modifier = Modifier.weight(8f),
                    text = todoTask.title,
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = todoTask.dueDateMillis,
                        color = MaterialTheme.colors.taskItemTextColor,
                        style = MaterialTheme.typography.caption,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = todoTask.description,
                color = MaterialTheme.colors.taskItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun TaskItemPreview() {
    TaskItem(
        todoTask = Task(
            id = 0,
            title = "Title",
            description = "Some random text",
            dueDateMillis = "27/7/2023"
        ),
        navigateToTaskScreen = {}
    )
}