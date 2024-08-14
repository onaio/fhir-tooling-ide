package org.smartregister.fct.engine.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import org.smartregister.fct.engine.ui.viewmodel.SubWindowViewModel

val LocalSubWindowViewModel = staticCompositionLocalOf { SubWindowViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }