package org.smartregister.fct.radiance.domain.model

class SingleFieldDialogResult(private val onShow: () -> Unit) {
    fun show() {
        onShow()
    }
}