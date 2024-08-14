package org.smartregister.fct.fm.ui.viewmodel

import okio.Path
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.domain.model.Applicable
import org.smartregister.fct.fm.domain.model.ContextMenu
import org.smartregister.fct.fm.domain.model.ContextMenuType
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.domain.handler.SystemFileHandler
import org.smartregister.fct.logger.FCTLogger

internal class SystemFileManagerViewModel(
    private val fileSystem: FileSystem
) :
    FileManagerViewModel(fileSystem), SystemFileHandler {

    fun getRootDirs() = fileSystem.rootDirs()

    override fun getContextMenuList(): List<ContextMenu> {
        return when(mode) {
            is FileManagerMode.Edit -> listOf(
                ContextMenu(ContextMenuType.CopyToInternal, Applicable.Both),
                ContextMenu(ContextMenuType.Upload, Applicable.File()),
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
            ContextMenuType.SelectFile -> onPathSelected(path)
            else -> {
                val error = "type ${contextMenu.menuType} is not handled"
                FCTLogger.w(error)
                Result.failure(IllegalStateException(error))
            }
        }
    }
}
