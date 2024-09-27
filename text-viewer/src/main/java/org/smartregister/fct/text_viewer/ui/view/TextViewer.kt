package org.smartregister.fct.text_viewer.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.aurora.presentation.ui.components.FloatingActionIconButton
import org.smartregister.fct.common.presentation.ui.container.Aurora
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.util.getKoinInstance
import org.smartregister.fct.text_viewer.ui.components.Editor
import org.smartregister.fct.text_viewer.ui.components.Toolbar

@Composable
fun TextViewer(
    text: String = "",
    formatOnStart: Boolean = false,
    callback: ((String) -> Unit)? = null,
    componentContext: ComponentContext
) {

    val appSetting = getKoinInstance<AppSettingManager>().appSetting
    val tabIndentState = remember { mutableStateOf(appSetting.codeEditorConfig.indent) }
    val textState = remember { mutableStateOf(text) }

    Aurora(
        componentContext = componentContext,
        fab = {
            callback?.let {
                FloatingActionIconButton(
                    icon = Icons.Outlined.Check,
                    onClick = {
                        it.invoke(textState.value)
                    }
                )
            }
        }
    ) {
        with(it) {
            Column {
                Toolbar(textState, tabIndentState)
                HorizontalDivider()
                Editor(textState, tabIndentState, formatOnStart)
            }
        }
    }

}





