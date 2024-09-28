package org.smartregister.fct.common.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.window.WindowState

val LocalWindowState =
    compositionLocalOf<WindowState> { error("window controller not provided") }