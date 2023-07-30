package com.wisnu.ubersnapandroidtask.navigation.destinations

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wisnu.ubersnapandroidtask.ui.screens.home.ListScreen
import com.wisnu.ubersnapandroidtask.ui.viewmodels.TaskViewModel
import com.wisnu.ubersnapandroidtask.utils.Action
import com.wisnu.ubersnapandroidtask.utils.LIST_ARGUMENT_KEY
import com.wisnu.ubersnapandroidtask.utils.LIST_SCREEN

@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    taskViewModel: TaskViewModel,
    action: Action
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) {
        ListScreen(navigateToTaskScreen = navigateToTaskScreen, taskViewModel = taskViewModel, action =action)
    }
}