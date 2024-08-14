package org.smartregister.fct.aurora.domain.controller

import org.smartregister.fct.aurora.ui.components.dialog.DialogType

class DialogController(private val onShow: () -> Unit, private val onHide: () -> Unit) {

    private var extras: MutableList<Any?> = mutableListOf()

    fun show(vararg extras: Any?) {
        this.extras.clear()
        this.extras.addAll(extras)
        onShow()
    }

    fun hide() {
        onHide()
    }

    fun getExtra() : List<Any?> = extras
}