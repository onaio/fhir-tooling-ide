package org.smartregister.fct.fm.domain.handler

import okio.Path

internal interface InAppFileHandler {
    suspend fun copy(source: Path) : Result<Unit>
}