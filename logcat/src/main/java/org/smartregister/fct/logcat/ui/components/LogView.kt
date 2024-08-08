package org.smartregister.fct.logcat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.smartregister.fct.logcat.domain.model.Log
import org.smartregister.fct.logcat.domain.model.LogLevel

@Composable
internal fun LogView(log: Log) {
    Row(modifier = Modifier) {
        DateView(log.dateTime)
        Spacer(Modifier.width(20.dp))
        TagView(log.tag)
        Spacer(Modifier.width(20.dp))
        LevelView(log.priority)
        Spacer(Modifier.width(10.dp))
        MessageView(log.message, log.priority.color)
    }
}


@Composable
private fun DateView(
    text: String
) {
    Text(
        modifier = Modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontFamily = FontFamily.Monospace
    )
}

@Composable
private fun TagView(
    text: String
) {
    MiddleEllipsisText(
        modifier = Modifier.width(150.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        softWrap = false,
        fontFamily = FontFamily.Monospace
    )
}

@Composable
private fun LevelView(
    priority: LogLevel
) {
    Box(
        modifier = Modifier.size(20.dp).background(priority.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier,
            text = priority.value,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun MessageView(
    text: String,
    color: Color
) {
    Text(
        modifier = Modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        softWrap = true,
        fontFamily = FontFamily.Monospace,
        color = color
    )
}
