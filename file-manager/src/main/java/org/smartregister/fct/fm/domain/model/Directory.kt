package org.smartregister.fct.fm.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import okio.Path

data class Directory(
    val icon: ImageVector? = null,
    val name: String,
    val path: Path
)
