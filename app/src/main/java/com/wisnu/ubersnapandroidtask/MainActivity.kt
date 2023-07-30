package com.wisnu.ubersnapandroidtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.wisnu.ubersnapandroidtask.navigation.SetupNavigation
import com.wisnu.ubersnapandroidtask.ui.theme.UbersnapAndroidTaskTheme
import com.wisnu.ubersnapandroidtask.ui.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController


    private val taskViewModel by viewModels<TaskViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UbersnapAndroidTaskTheme {
                // navController = rememberNavController()
                navController = rememberAnimatedNavController()
                SetupNavigation(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    changeAction = taskViewModel::changeAction,
                    action = taskViewModel.action.value
                )
            }
        }
    }
}

