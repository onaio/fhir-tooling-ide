package org.smartregister.fct.aurora.domain.controller

class ConfirmationDialogController(
    private val onShow: ConfirmationDialogController.(
        title: String?,
        message: String,
        extras: List<Any?>
    ) -> Unit,
    private val onHide: ConfirmationDialogController.() -> Unit)
{

    fun show(title: String? = null, message: String, vararg extras: Any?) {
        onShow(title, message, extras.asList())
    }

    fun hide() {
        onHide()
    }
}