package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import okio.Path.Companion.toPath
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.logcat.FCTLogger
import java.io.File
import java.lang.RuntimeException

class InAppFileManagerViewModel(fileSystem: FileSystem) :
    FileManagerViewModel(fileSystem) {

    suspend fun createNewFolder(folderName: String) : Result<Unit> {
        val activePath = getActivePath().value
        val newFolderPath = "${activePath}${File.separator}$folderName".toPath()

        return try {
            okioFileSystem.createDirectory(newFolderPath, true)
            setActivePath(activePath)
            Result.success(Unit)
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(RuntimeException("$folderName already exists in ${activePath.name} folder"))
        }

    }
}
