package org.smartregister.fct.engine.domain.model

class SingleFieldDialogResult(private val onShow: () -> Unit) {
    fun show() {
        onShow()
    }
}