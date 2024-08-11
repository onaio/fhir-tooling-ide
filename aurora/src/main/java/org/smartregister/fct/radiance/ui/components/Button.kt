package org.smartregister.fct.radiance.ui.components

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button as Mat3Button
import androidx.compose.material3.TextButton as Mat3TextButton
import androidx.compose.material3.OutlinedButton as Mat3OutlinedButton

enum class ButtonType {
    Button, OutlineButton, TextButton
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector? = null,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    Mat3Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enable
    ) {
        icon?.let {
            Icon(
                icon, contentDescription = null,
            )

            Spacer(Modifier.width(8.dp))
        }

        Text(
            text = label,
        )
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector? = null,
    enable: Boolean = true,
    textAlign: TextAlign? = null,
    selected: Boolean = false,
    selectedContainerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit
) {

    val content: @Composable (RowScope.() -> Unit) = {
        icon?.let {
            Icon(
                icon = icon,
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.width(8.dp))
        }

        if (textAlign == TextAlign.Start) {
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = textAlign
            )
        } else {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    if (selected) {
        Mat3TextButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enable,
            colors = ButtonDefaults.buttonColors(
                containerColor = selectedContainerColor
            ),
            content = content
        )
    } else {
        Mat3TextButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enable,
            content = content
        )
    }

}

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector? = null,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    Mat3OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enable
    ) {
        icon?.let {
            Icon(
                icon, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.width(8.dp))
        }

        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

