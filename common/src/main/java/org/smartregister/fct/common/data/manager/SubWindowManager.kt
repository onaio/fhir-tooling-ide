package org.smartregister.fct.common.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.common.data.enums.BottomWindowState
import org.smartregister.fct.common.data.enums.RightWindowState


class SubWindowViewModel {

    private val rightWindowState = MutableStateFlow<RightWindowState?>(null)
    private val bottomWindowState = MutableStateFlow<BottomWindowState?>(null)

    fun getRightWindowState(): StateFlow<RightWindowState?> = rightWindowState

    fun setRightWindowState(state: RightWindowState?) {
        CoroutineScope(Dispatchers.IO).launch {
            rightWindowState.emit(if (state == rightWindowState.value) null else state)
        }
    }

    fun getBottomWindowState(): StateFlow<BottomWindowState?> = bottomWindowState

    fun setBottomWindowState(state: BottomWindowState?) {
        CoroutineScope(Dispatchers.IO).launch {
            bottomWindowState.emit(if (state == bottomWindowState.value) null else state)
        }
    }
}