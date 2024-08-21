package org.smartregister.fct.aurora.domain.manager

import org.smartregister.fct.aurora.domain.model.Message

interface AuroraManager {

    fun showSnackbar(text: String?, onDismiss: (() -> Unit)? = null)

    fun showSnackbar(message: Message, onDismiss: (() -> Unit)? = null)
}