package org.smartregister.fct.fm.ui.viewmodel

import okio.Path
import okio.Path.Companion.toPath
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.domain.handler.InAppFileHandler
import org.smartregister.fct.fm.domain.model.Applicable
import org.smartregister.fct.fm.domain.model.ContextMenu
import org.smartregister.fct.fm.domain.model.ContextMenuType
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.logger.FCTLogger
import java.io.File

internal class InAppFileManagerViewModel(fileSystem: FileSystem) :
    FileManagerViewModel(fileSystem), InAppFileHandler {

    suspend fun createNewFolder(folderName: String): Result<Unit> {
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

    private suspend fun deletePath(path: Path): Result<Unit> {
        return try {
            okioFileSystem.deleteRecursively(path, true)
            setActivePath(getActivePath().value)
            Result.success(Unit)
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }

    override fun getContextMenuList(): List<ContextMenu> {
        return when (mode) {
            is FileManagerMode.Edit -> listOf(
                ContextMenu(ContextMenuType.Upload, Applicable.File()),
                ContextMenu(ContextMenuType.Delete, Applicable.Both),
            )

            is FileManagerMode.View -> {
                listOf(
                    ContextMenu(
                        ContextMenuType.SelectFile,
                        Applicable.File((mode as FileManagerMode.View).extensions)
                    )
                )
            }
        }
    }

    override suspend fun onContextMenuClick(contextMenu: ContextMenu, path: Path): Result<Unit> {
        return when (contextMenu.menuType) {
            ContextMenuType.Delete -> deletePath(path)
            ContextMenuType.SelectFile -> onPathSelected(path)
            else -> {
                val error = "type ${contextMenu.menuType} is not handled"
                FCTLogger.w(error)
                Result.failure(IllegalStateException(error))
            }
        }
    }

    override suspend fun copy(source: Path): Result<Unit> {
        return copy(source, getActivePath().value)
    }
}
