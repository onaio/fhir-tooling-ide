package org.smartregister.fct.engine.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FCTThemeViewModel {

    private var _isLightTheme = true
    private val isLightTheme = MutableStateFlow(_isLightTheme)

    fun themeState(): StateFlow<Boolean> = isLightTheme

    fun isLightTheme() = _isLightTheme

    fun switchTheme() {
        CoroutineScope(Dispatchers.IO).launch {
            _isLightTheme = !_isLightTheme
            isLightTheme.emit(_isLightTheme)
        }
    }
}