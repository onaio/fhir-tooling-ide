package org.smartregister.fct.engine.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import org.smartregister.fct.engine.data.viewmodel.AppSettingViewModel
import org.smartregister.fct.engine.data.viewmodel.SubWindowViewModel

val LocalAppSettingViewModel = staticCompositionLocalOf { AppSettingViewModel() }
val LocalSubWindowViewModel = staticCompositionLocalOf { SubWindowViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }