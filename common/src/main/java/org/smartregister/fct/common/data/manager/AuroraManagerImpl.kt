package org.smartregister.fct.common.data.manager

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.common.domain.model.Message
import org.smartregister.fct.engine.domain.model.ServerConfig
import org.smartregister.fct.engine.util.componentScope

internal typealias ServerConfigOption = Pair<ServerConfig?, (ServerConfig) -> Unit>

internal class AuroraManagerImpl(
    componentContext: ComponentContext
) : AuroraManager, ComponentContext by componentContext {

    private val _showSnackbar = MutableStateFlow<Message?>(null)
    val showSnackbar: StateFlow<Message?> = _showSnackbar

    private val _showLoader = MutableStateFlow(false)
    val showLoader: StateFlow<Boolean> = _showLoader

    private val _showServerConfigDialog = MutableStateFlow<ServerConfigOption?>(null)
    val showServerConfigDialog: StateFlow<ServerConfigOption?> = _showServerConfigDialog

    override fun showSnackbar(text: String?, onDismiss: (() -> Unit)?) {
        text?.let {
            showSnackbar(Message.Info(text), onDismiss)
        }
    }

    override fun showSnackbar(message: Message, onDismiss: (() -> Unit)?) {
        componentScope.launch {
            if (message.text.trim().isNotEmpty()) {
                _showSnackbar.emit(null)
                _showSnackbar.emit(message)
                delay(3000)
                _showSnackbar.emit(null)
                onDismiss?.invoke()
            }
        }
    }

    override fun showLoader() {
        componentScope.launch {
            _showLoader.emit(true)
        }
    }

    override fun hideLoader() {
        componentScope.launch {
            _showLoader.emit(false)
        }
    }

    override fun selectServerConfig(
        initialConfig: ServerConfig?,
        onSelected: (ServerConfig) -> Unit
    ) {
        componentScope.launch {
            _showServerConfigDialog.emit(
                Pair(initialConfig, onSelected)
            )
        }
    }

    fun resetServerConfig() {
        componentScope.launch {
            _showServerConfigDialog.emit(null)
        }
    }
}