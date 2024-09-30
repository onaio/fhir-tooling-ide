package org.smartregister.fct.common.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

val LocalWindowState =
    compositionLocalOf<WindowState> { error("window controller not provided") }

val windowTitle = MutableStateFlow("Dashboard")