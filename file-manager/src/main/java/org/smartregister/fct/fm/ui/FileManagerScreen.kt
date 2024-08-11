package org.smartregister.fct.fm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class FileManagerScreen : Screen {

    @Composable
    override fun Content() {

        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) {
                SystemFileManager()
            }
            Box(Modifier.weight(1f)) {
                InAppFileManager()
            }
        }

    }

}