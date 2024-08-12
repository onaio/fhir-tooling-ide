package org.smartregister.fct.aurora.domain.controller

class SingleFieldDialogController(private val onShow: () -> Unit) {
    fun show() {
        onShow()
    }
}