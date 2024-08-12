package org.smartregister.fct.aurora.domain.controller

import org.smartregister.fct.aurora.ui.components.dialog.DialogType

class DialogController(private val onShow: (dialogDialog: DialogType) -> Unit, private val onHide: () -> Unit) {

    private var extra: Any? = null

    fun show(extra: Any? = null, dialogDialog: DialogType = DialogType.Default) {
        this.extra = extra
        onShow(dialogDialog)
    }

    fun hide() {
        onHide()
    }

    fun<T> getExtra() : T? = extra as T?
}