package org.smartregister.fct.aurora.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
        val alpha = if(enable) 1f else 0.5f
        icon?.let {
            Icon(
                icon = icon,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
            )

            Spacer(Modifier.width(8.dp))
        }

        if (textAlign == TextAlign.Start) {
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                textAlign = textAlign
            )
        } else {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
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
        enabled = enable,
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = if (enable) ButtonDefaults.outlinedButtonBorder.brush else SolidColor(MaterialTheme.colorScheme.surface)
        )
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
            color = if (enable) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

