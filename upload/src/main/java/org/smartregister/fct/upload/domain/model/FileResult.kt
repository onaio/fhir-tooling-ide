package org.smartregister.fct.upload.domain.model

import io.github.vinceglb.filekit.core.PlatformFile

data class FileResult(
    val text: String,
    val platformFile: PlatformFile
)