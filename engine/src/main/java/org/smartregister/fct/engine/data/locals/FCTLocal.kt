package org.smartregister.fct.engine.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import org.smartregister.fct.engine.data.viewmodel.FCTThemeViewModel
import org.smartregister.fct.engine.data.viewmodel.WindowViewModel

val LocalFCTTheme = staticCompositionLocalOf { FCTThemeViewModel() }
val LocalWindowViewModel = staticCompositionLocalOf { WindowViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }