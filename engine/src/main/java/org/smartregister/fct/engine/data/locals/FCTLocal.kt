package org.smartregister.fct.engine.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import org.smartregister.fct.engine.data.viewmodel.AppSettingViewModel
import org.smartregister.fct.engine.data.viewmodel.WindowViewModel

val LocalAppSettingViewModel = staticCompositionLocalOf { AppSettingViewModel() }
val LocalWindowViewModel = staticCompositionLocalOf { WindowViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }