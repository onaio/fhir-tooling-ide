package org.smartregister.fct.engine.util

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope

fun ComponentContext.componentScope(dispatcher: MainCoroutineDispatcher = Dispatchers.Main.immediate) : CoroutineScope {
    return coroutineScope(dispatcher + SupervisorJob())
}

val ComponentContext.componentScope: CoroutineScope
    get() = componentScope()