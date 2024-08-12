package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import org.smartregister.fct.fm.domain.datasource.FileSystem

abstract class FileManagerViewModel(private val fileSystem: FileSystem) {

    protected val showHiddenFile = MutableStateFlow(false)
    protected val okioFileSystem = okio.FileSystem.SYSTEM
    protected val activeDir = MutableStateFlow(fileSystem.defaultActivePath())
    private val activeDirContent = MutableStateFlow(getFilteredPathList(activeDir.value))

    fun getCommonDirs() = fileSystem.commonDirs()

    fun getActivePath(): StateFlow<Path> = activeDir

    fun getActivePathContent(): StateFlow<List<Path>> = activeDirContent

    suspend fun setActivePath(path: Path) {
        activeDir.emit(path)
        activeDirContent.emit(getFilteredPathList(path))
    }

    open fun getFilteredPathList(path: Path): List<Path> = okioFileSystem.list(path)

}
