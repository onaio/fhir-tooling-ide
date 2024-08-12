package org.smartregister.fct.aurora.domain.controller

class LoaderDialogController(private val onShow: () -> Unit, private val onHide: () -> Unit) {

    fun show() {
        onShow()
    }

    fun hide() {
        onHide()
    }
}