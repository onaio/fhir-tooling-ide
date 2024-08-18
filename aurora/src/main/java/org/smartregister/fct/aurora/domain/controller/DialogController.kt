package org.smartregister.fct.aurora.domain.controller

class DialogController<T>(
    private val onShow: DialogController<T>.(
        data: T?
    ) -> Unit,
    private val onHide: DialogController<T>.() -> Unit)
{

    fun show(data: T? = null) {
        onShow(data)
    }

    fun hide() {
        onHide()
    }
}