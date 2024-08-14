package org.smartregister.fct.fm.ui.viewmodel

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.Path
import okio.Path.Companion.toPath
import org.apache.commons.io.FileUtils
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.domain.model.ContextMenu
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.logger.FCTLogger
import java.io.File
import java.nio.charset.Charset


internal abstract class FileManagerViewModel(private val fileSystem: FileSystem) {

    private val showHiddenFile = MutableStateFlow(false)
    protected val okioFileSystem = okio.FileSystem.SYSTEM
    private val activeDir = MutableStateFlow(fileSystem.defaultActivePath())
    private val activeDirContent = MutableStateFlow(getFilteredPathList(activeDir.value))
    var mode: FileManagerMode = FileManagerMode.Edit
        private set
    var visibleItem = MutableStateFlow(true)
        private set

    fun getCommonDirs() = fileSystem.commonDirs()

    fun getActivePath(): StateFlow<Path> = activeDir

    fun getActivePathContent(): StateFlow<List<Path>> = activeDirContent

    suspend fun setActivePath(path: Path) {
        visibleItem.emit(false)
        activeDir.emit(path)
        delay(100)
        activeDirContent.emit(getFilteredPathList(path))
        visibleItem.emit(true)
    }

    suspend fun setShowHiddenFile(isShowHiddenFile: Boolean) {
        showHiddenFile.emit(isShowHiddenFile)
        setActivePath(activeDir.value)
    }

    fun getShowHiddenFile(): StateFlow<Boolean> = showHiddenFile

    private fun getFilteredPathList(path: Path): List<Path> = okioFileSystem
        .list(path)
        .filter {
            if (it.toFile().isHidden) showHiddenFile.value else true
        }
        .filter {
            if (mode is FileManagerMode.View && it.toFile().isFile) {
                it.toFile().extension in (mode as FileManagerMode.View).extensions
            } else true
        }

    @OptIn(DelicateCoroutinesApi::class)
    fun setMode(mode: FileManagerMode) {
        if (mode != this.mode) {
            val defaultPath = fileSystem.defaultActivePath()
            this.mode = mode
            GlobalScope.launch(Dispatchers.IO) {
                activeDir.emit(defaultPath)
                activeDirContent.emit(getFilteredPathList(defaultPath))
            }
        }

    }

    suspend fun copy(source: Path, dest: Path): Result<Unit> {
        return try {
            val sourceFile = source.toFile()
            val destFile = "${dest}${File.separator}${source.name}".toPath().toFile()

            if (sourceFile.isFile) {
                FileUtils.copyFile(sourceFile, destFile)
            } else {
                withContext(Dispatchers.IO) {
                    FileUtils.copyDirectory(sourceFile, destFile)
                }
            }
            setActivePath(dest)
            Result.success(Unit)
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }

    }

    suspend fun onDoubleClick(path: Path) {
        if (path.toFile().isDirectory) {
            setActivePath(path)
        } else {
            onPathSelected(path)
        }
    }

    suspend fun onPathSelected(path: Path) : Result<Unit> {
        return try {
            if (mode is FileManagerMode.View) {
                val viewMode = mode as FileManagerMode.View
                val fileSelected = viewMode.onFileSelected
                val pathSelected = viewMode.onPathSelected

                fileSelected?.let {
                    withContext(Dispatchers.IO) {
                        fileSelected(FileUtils.readFileToString(path.toFile(), Charset.defaultCharset()))
                    }
                }

                pathSelected?.invoke(path)
            }

            return Result.success(Unit)
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }

    abstract fun getContextMenuList(): List<ContextMenu>
    abstract suspend fun onContextMenuClick(contextMenu: ContextMenu, path: Path): Result<Unit>
}
