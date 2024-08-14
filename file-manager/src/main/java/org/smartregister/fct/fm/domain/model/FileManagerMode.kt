package org.smartregister.fct.fm.domain.model

import okio.Path

sealed class FileManagerMode {
    class View(
        val onFileSelected: ((String) -> Unit)? = null,
        val onPathSelected: ((Path) -> Unit)? = null,
        val extensions: List<String> = listOf(),
    ) : FileManagerMode()
    data object Edit : FileManagerMode()
}