package org.smartregister.fct.common.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.common.presentation.viewmodel.SubWindowViewModel

val LocalRootComponent = staticCompositionLocalOf<ComponentContext?> { null }
val LocalSubWindowViewModel = staticCompositionLocalOf { SubWindowViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }