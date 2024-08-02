package org.smartregister.fct.engine.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import org.smartregister.fct.engine.data.viewmodel.FCTThemeViewModel
import org.smartregister.fct.engine.data.viewmodel.RightNavigationViewModel

val LocalFCTTheme = staticCompositionLocalOf { FCTThemeViewModel() }
val LocalRightNavigationViewModel = staticCompositionLocalOf { RightNavigationViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }