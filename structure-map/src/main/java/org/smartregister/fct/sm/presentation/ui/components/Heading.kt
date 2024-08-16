package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Heading() {
    Box(
        Modifier.fillMaxWidth().height(40.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Structure Map",
            style = MaterialTheme.typography.titleSmall
        )
        VerticalDivider(modifier = Modifier.align(Alignment.CenterEnd))
        HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
    }
}