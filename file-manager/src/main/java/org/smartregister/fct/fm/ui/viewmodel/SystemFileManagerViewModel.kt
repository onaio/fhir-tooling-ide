package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import org.smartregister.fct.fm.domain.datasource.FileSystem

class SystemFileManagerViewModel(private val fileSystem: FileSystem) {

    private val showHiddenFile = MutableStateFlow(false)
    private val okioFileSystem = okio.FileSystem.SYSTEM
    private val activeDir = MutableStateFlow(fileSystem.defaultActivePath())
    private val activeDirContent = MutableStateFlow(getFilteredPathList(activeDir.value))

    fun getCommonDirs() = fileSystem.commonDirs()
    fun getRootDirs() = fileSystem.rootDirs()

    fun getActivePath(): StateFlow<Path> = activeDir

    fun getActivePathContent(): StateFlow<List<Path>> = activeDirContent

    suspend fun setActivePath(path: Path) {
        activeDir.emit(path)
        activeDirContent.emit(getFilteredPathList(path))
    }

    suspend fun setShowHiddenFile(isShowHiddenFile: Boolean) {
        showHiddenFile.emit(isShowHiddenFile)
        setActivePath(activeDir.value)
    }

    fun getShowHiddenFile(): StateFlow<Boolean> = showHiddenFile

    private fun getFilteredPathList(path: Path) : List<Path> = okioFileSystem
        .list(path)
        .filter {
            if(it.toFile().isHidden) showHiddenFile.value else true
        }
}
