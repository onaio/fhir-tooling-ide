package org.smartregister.fct.fm.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.smartregister.fct.fm.presentation.components.FileManagerScreenComponent
import org.smartregister.fct.fm.presentation.ui.components.InAppFileManager
import org.smartregister.fct.fm.presentation.ui.components.SystemFileManager

@Composable
fun FileManagerScreen(component: FileManagerScreenComponent) {

    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            SystemFileManager(component)
        }
        VerticalDivider()
        Box(Modifier.weight(1f)) {
            InAppFileManager(component)
        }
    }

}

