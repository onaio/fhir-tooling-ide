package org.smartregister.fct.configs.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.smartregister.fct.configs.data.helper.ConfigComponentState
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import java.util.Stack


class ComponentNavigator {

    private val state = MutableStateFlow<ConfigComponentState?>(null)
    private val stateHistory = Stack<ConfigComponentState>()
    var isContainerExpanded = true
        private set

    fun getState(): StateFlow<ConfigComponentState?> {
        return state
    }

    fun pushState(newState: ConfigComponentState, includeInHistory: Boolean = true) {
        CoroutineScope(Dispatchers.IO).launch {
            if (includeInHistory) stateHistory.push(newState)
            state.emit(newState)
        }
    }

    fun updateCurrentState(updatedState: ConfigComponentState) {
        CoroutineScope(Dispatchers.IO).launch {
            stateHistory[stateHistory.size - 1] = updatedState
            state.emit(updatedState)
        }
    }

    fun reload() {
        CoroutineScope(Dispatchers.IO).launch {
            state.collectLatest {
                state.emit(it)
            }
        }
    }

    fun popState() {
        CoroutineScope(Dispatchers.IO).launch {
            stateHistory.pop()
            state.emit(stateHistory.last())
        }
    }

    fun getStateHistory(): List<ConfigComponentState> {
        return stateHistory
    }

    fun clearHistory() {
        stateHistory.clear()
    }

    fun getLastStateFromHistory(): ConfigComponentState? {
        return if (stateHistory.isEmpty()) null else stateHistory.last()
    }

    fun setContainerExpanded(expanded: Boolean) {
        isContainerExpanded = expanded
    }
}

class RegisterConfigViewModel(configWrapper: ConfigWrapper) : ConfigViewModel(configWrapper) {
    val fhirResourceNavigator = ComponentNavigator()
    val secondaryResourcesNavigator = ComponentNavigator()
}

abstract class ConfigViewModel(val configWrapper: ConfigWrapper) {

}