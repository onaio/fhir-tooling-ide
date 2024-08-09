package org.smartregister.fct.radiance.domain.model

class DialogController(private val onShow: () -> Unit, private val onHide: () -> Unit) {

    private var extra: Any? = null

    fun show(extra: Any? = null) {
        this.extra = extra
        onShow()
    }

    fun hide() {
        onHide()
    }

    fun<T> getExtra() : T? = extra as T?
}