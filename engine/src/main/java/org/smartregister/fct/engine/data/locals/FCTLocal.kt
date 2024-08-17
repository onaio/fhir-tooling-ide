package org.smartregister.fct.engine.data.locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.engine.presentation.viewmodel.SubWindowViewModel

val LocalRootComponent = staticCompositionLocalOf<ComponentContext?> { null }
val LocalSubWindowViewModel = staticCompositionLocalOf { SubWindowViewModel() }
val LocalSnackbarHost = staticCompositionLocalOf { SnackbarHostState() }