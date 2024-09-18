package org.smartregister.fct.common.presentation.ui.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.common.data.locals.AuroraLocal
import org.smartregister.fct.common.data.manager.AuroraManagerImpl
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.common.domain.model.Message
import org.smartregister.fct.common.presentation.ui.dialog.rememberLoaderDialogController
import org.smartregister.fct.common.presentation.ui.dialog.rememberServerConfigProviderDialog

@Composable
fun Aurora(
    componentContext: ComponentContext,
    modifier: Modifier = Modifier,
    fab: @Composable (() -> Unit)? = null,
    content: @Composable AuroraManager.() -> Unit
) {

    // create aurora manager instance
    val aurora = remember(componentContext) { AuroraManagerImpl(componentContext) }

    // full screen loader dialog controller
    val loaderController = rememberLoaderDialogController()

    // server config selection dialog controller
    val serverConfigProviderDialog = rememberServerConfigProviderDialog(
        componentContext = componentContext,
    ) {
        val callback = aurora.showServerConfigDialog.value?.second
        aurora.resetServerConfig()
        callback?.invoke(it)
    }

    val showLoader by aurora.showLoader.collectAsState()
    val showServerConfigDialog by aurora.showServerConfigDialog.collectAsState()

    // control loader dialog visibility
    if (showLoader) loaderController.show() else loaderController.hide()

    if (showServerConfigDialog != null) {
        serverConfigProviderDialog.show(showServerConfigDialog!!.first)
    } else {
        serverConfigProviderDialog.hide()
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = { fab?.invoke() }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            CompositionLocalProvider(AuroraLocal provides aurora) {
                content(aurora)
            }

            Snackbar(aurora)
        }
    }

}

context (BoxScope)
@Composable
private fun Snackbar(aurora: AuroraManagerImpl) {

    val showSnackbar by aurora.showSnackbar.collectAsState()



    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp),
        visible = showSnackbar != null,
        enter = slideInVertically { it / 2 } + fadeIn(),
        exit = slideOutVertically { it / 2 } + fadeOut()
    ) {

        val text = remember { showSnackbar!!.text }

        val bg = when (showSnackbar) {
            is Message.Error -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.primary
        }

        Card(
            modifier = Modifier.widthIn(min = 200.dp, max = 600.dp),
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = bg
            )
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = text,
            )
        }
    }

}