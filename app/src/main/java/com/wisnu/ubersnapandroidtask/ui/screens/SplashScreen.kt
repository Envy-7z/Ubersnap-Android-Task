package com.wisnu.ubersnapandroidtask.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wisnu.ubersnapandroidtask.R
import com.wisnu.ubersnapandroidtask.ui.theme.UbersnapAndroidTaskTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToListScreen: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val offsetState by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 100.dp,
        animationSpec = tween(
            durationMillis = 1000,
        )
    )

    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(key1 = true, block = {
        startAnimation = true
        // delay of 3000 ms
        delay(3000)
        navigateToListScreen()
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(150.dp)
                .offset(y = offsetState)
                .alpha(alpha = alphaState),
            painter = painterResource(id = getLogo()),
            contentDescription = "To Do app Logo"
        )
    }
}

@Composable
fun getLogo(): Int {
    return if (isSystemInDarkTheme()) {
        R.drawable.baseline_cruelty_free_24
    } else {
        R.drawable.baseline_cruelty_free_24
    }
}


@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navigateToListScreen = {})
}

@Preview
@Composable
fun PreviewSplashScreenDarkTeam() {
    UbersnapAndroidTaskTheme(darkTheme = true) {
        SplashScreen(navigateToListScreen = {})
    }
}