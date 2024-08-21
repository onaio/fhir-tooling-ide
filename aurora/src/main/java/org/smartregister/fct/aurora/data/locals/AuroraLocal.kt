package org.smartregister.fct.aurora.data.locals

import androidx.compose.runtime.compositionLocalOf
import org.smartregister.fct.aurora.domain.manager.AuroraManager

val AuroraLocal = compositionLocalOf<AuroraManager?> { null }