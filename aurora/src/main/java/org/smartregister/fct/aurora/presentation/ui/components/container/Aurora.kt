package org.smartregister.fct.aurora.presentation.ui.components.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import org.smartregister.fct.aurora.data.locals.AuroraLocal
import org.smartregister.fct.aurora.data.manager.AuroraManagerImpl
import org.smartregister.fct.aurora.domain.model.Message

@Composable
fun Aurora(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()
    val aurora = remember { AuroraManagerImpl(scope) }
    val showSnackbar by aurora.showSnackbar.collectAsState()

    Box(
        modifier = modifier
    ) {

        CompositionLocalProvider(AuroraLocal provides aurora) {
            content()
        }

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
                modifier = Modifier.fillMaxWidth(),
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
}