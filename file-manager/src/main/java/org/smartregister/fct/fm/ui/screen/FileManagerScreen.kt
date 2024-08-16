package org.smartregister.fct.fm.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.smartregister.fct.fm.ui.components.InAppFileManager
import org.smartregister.fct.fm.ui.components.SystemFileManager

@Composable
fun FileManagerScreen() {

    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            SystemFileManager()
        }
        VerticalDivider()
        Box(Modifier.weight(1f)) {
            InAppFileManager()
        }
    }

}

