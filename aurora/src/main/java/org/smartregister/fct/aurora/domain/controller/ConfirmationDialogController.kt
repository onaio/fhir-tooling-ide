package org.smartregister.fct.aurora.domain.controller

class ConfirmationDialogController<T>(internal val onShow: (extra: T?) -> Unit) {

    internal var extra: T? = null

    fun show(extra: T? = null) {
        this.extra = extra
        onShow(extra)
    }
}