package org.smartregister.fct.fm.presentation.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.Path
import okio.Path.Companion.toPath
import org.apache.commons.io.FileUtils
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.domain.model.ContextMenu
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.logger.FCTLogger
import java.io.File
import java.nio.charset.Charset


internal abstract class FileManagerComponent(
    protected val componentContext: ComponentContext,
    val fileSystem: FileSystem,
    val mode: FileManagerMode
) : ComponentContext by componentContext {

    private val showHiddenFile = MutableStateFlow(false)
    protected val okioFileSystem = okio.FileSystem.SYSTEM
    private val activeDir = MutableStateFlow(fileSystem.defaultActivePath())
    private val activeDirContent = MutableStateFlow(getFilteredPathList(activeDir.value))

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

    suspend fun copy(source: Path, dest: Path) {
        try {
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
        } catch (ex: Exception) {
            FCTLogger.e(ex)
        }

    }

    fun onDoubleClick(path: Path) {
        componentScope.launch {
            if (path.toFile().isDirectory) {
                setActivePath(path)
            } else {
                onPathSelected(path)
            }
        }
    }

    suspend fun onPathSelected(path: Path) : Result<Unit> {
        return try {
            if (mode is FileManagerMode.View) {
                val fileSelected = mode.onFileSelected
                val pathSelected = mode.onPathSelected

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
