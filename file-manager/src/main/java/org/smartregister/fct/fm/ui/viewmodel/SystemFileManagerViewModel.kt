package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import org.smartregister.fct.fm.domain.datasource.FileSystem

internal class SystemFileManagerViewModel(private val fileSystem: FileSystem) {

    private val activeDir = MutableStateFlow(fileSystem.home())

    fun getCommonDirs() = fileSystem.commonDirs()

    fun getActiveDir(): StateFlow<Path> = activeDir

    suspend fun setActiveDir(path: Path) {

    }
}