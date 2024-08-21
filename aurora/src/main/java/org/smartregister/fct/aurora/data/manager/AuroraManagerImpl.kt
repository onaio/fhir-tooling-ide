package org.smartregister.fct.aurora.data.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.domain.manager.AuroraManager
import org.smartregister.fct.aurora.domain.model.Message

internal class AuroraManagerImpl(private val scope: CoroutineScope) : AuroraManager{

    private val _showSnackbar = MutableStateFlow<Message?>(null)
    val showSnackbar: StateFlow<Message?> = _showSnackbar

    override fun showSnackbar(text: String?, onDismiss: (() -> Unit)?) {
       text?.let {
           showSnackbar(Message.Info(text), onDismiss)
       }
    }

    override fun showSnackbar(message: Message, onDismiss: (() -> Unit)?) {
        scope.launch {
            if (message.text.trim().isNotEmpty()) {
                _showSnackbar.emit(null)
                _showSnackbar.emit(message)
                delay(3000)
                _showSnackbar.emit(null)
                onDismiss?.invoke()
            }
        }
    }
}