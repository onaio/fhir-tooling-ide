package org.smartregister.fct.engine.domain.model

class ConfirmationDialogResult<T>(internal val onShow: (extra: T?) -> Unit) {

    internal var extra: T? = null

    fun show(extra: T? = null) {
        this.extra = extra
        onShow(extra)
    }
}