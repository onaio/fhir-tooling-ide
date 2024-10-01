package org.smartregister.fct.base64.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedButton

@Composable
internal fun TabIndentButton(tabIndentState: MutableState<Int>) {

    ActionButton(
        label = "${tabIndentState.value} Space Indent",
        onClick = {
            tabIndentState.value = if (tabIndentState.value == 4) 2 else 4
        },
    )
}