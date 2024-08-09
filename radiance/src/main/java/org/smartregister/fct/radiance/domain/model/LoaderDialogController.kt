package org.smartregister.fct.radiance.domain.model

class LoaderDialogController(private val onShow: () -> Unit, private val onHide: () -> Unit) {

    fun show() {
        onShow()
    }

    fun hide() {
        onHide()
    }
}