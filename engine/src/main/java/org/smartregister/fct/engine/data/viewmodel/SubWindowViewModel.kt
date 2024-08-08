package org.smartregister.fct.engine.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.engine.data.enums.BottomWindowState
import org.smartregister.fct.engine.data.enums.LeftWindowState
import org.smartregister.fct.engine.data.enums.RightWindowState


class SubWindowViewModel {

    private val leftWindowState = MutableStateFlow<LeftWindowState?>(null)
    private val rightWindowState = MutableStateFlow<RightWindowState?>(null)
    private val bottomWindowState = MutableStateFlow<BottomWindowState?>(null)

    fun getLeftWindowState(): StateFlow<LeftWindowState?> = leftWindowState

    fun setLeftWindowState(state: LeftWindowState?) {
        CoroutineScope(Dispatchers.IO).launch {
            leftWindowState.emit(state)
        }
    }

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