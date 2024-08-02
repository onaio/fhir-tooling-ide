package org.smartregister.fct.engine.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RightNavigationViewModel {
    private val openLogWindow = MutableStateFlow(false)

    fun getLogWindowState(): StateFlow<Boolean> = openLogWindow

    fun toggleLogWindow() {
        CoroutineScope(Dispatchers.IO).launch {
            openLogWindow.emit(!openLogWindow.value)
        }
    }
}