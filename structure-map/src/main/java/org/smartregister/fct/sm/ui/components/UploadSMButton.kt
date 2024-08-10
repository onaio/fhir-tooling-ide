package org.smartregister.fct.sm.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
internal fun UploadSMButton() {
    var showButton by remember { mutableStateOf(false) }

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = showButton,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            SMUploadButton(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(showButton) {
        delay(300)
        showButton = true
    }
}