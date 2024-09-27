package org.smartregister.fct.rules.util

import androidx.compose.ui.unit.IntOffset
import org.smartregister.fct.rules.domain.model.Workspace

internal object WorkspaceConfig {
    var workspace: Workspace? = null
    var defaultBoardScale = 1f
    var defaultBoardOffset = IntOffset.Zero
    var showConnection: Boolean = true
}