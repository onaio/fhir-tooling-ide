package org.smartregister.fct.fm.domain.model

import okio.Path

sealed class FileManagerMode(val activeDirPath: String?) {
    data class View(
        val defaultDirPath: String? = null,
        val onFileSelected: ((String, String) -> Unit)? = null,
        val onPathSelected: ((String, String) -> Unit)? = null,
        val extensions: List<String> = listOf(),
    ) : FileManagerMode(defaultDirPath)
    data class Edit(
        val defaultDirPath: String? = null,
    ) : FileManagerMode(defaultDirPath)
}