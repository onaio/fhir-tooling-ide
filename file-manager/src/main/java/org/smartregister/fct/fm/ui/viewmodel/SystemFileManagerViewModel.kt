package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import okio.SYSTEM
import org.smartregister.fct.fm.domain.datasource.FileSystem

internal class SystemFileManagerViewModel(private val fileSystem: FileSystem) {

    private val okioFileSystem = okio.FileSystem.SYSTEM
    private val activeDir = MutableStateFlow(fileSystem.defaultActivePath())
    private val activeDirContent = MutableStateFlow(okioFileSystem.list(activeDir.value))

    fun getCommonDirs() = fileSystem.commonDirs()
    fun getRootDirs() = fileSystem.rootDirs()

    fun getActiveDir(): StateFlow<Path> = activeDir

    fun getActiveDirContent(): StateFlow<List<Path>> = activeDirContent

    suspend fun setActiveDir(path: Path) {
        activeDir.emit(path)
        activeDirContent.emit(okioFileSystem.list(path))
    }
}