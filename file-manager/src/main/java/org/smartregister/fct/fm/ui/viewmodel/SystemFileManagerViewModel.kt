package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import org.smartregister.fct.fm.domain.datasource.FileSystem

class SystemFileManagerViewModel(private val fileSystem: FileSystem) :
    FileManagerViewModel(fileSystem) {

    fun getRootDirs() = fileSystem.rootDirs()

    suspend fun setShowHiddenFile(isShowHiddenFile: Boolean) {
        showHiddenFile.emit(isShowHiddenFile)
        setActivePath(activeDir.value)
    }

    fun getShowHiddenFile(): StateFlow<Boolean> = showHiddenFile

    override fun getFilteredPathList(path: Path): List<Path> = okioFileSystem
        .list(path)
        .filter {
            if(it.toFile().isHidden) showHiddenFile.value else true
        }
}
