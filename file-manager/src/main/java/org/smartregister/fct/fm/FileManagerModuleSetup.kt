package org.smartregister.fct.fm

import okio.Path.Companion.toPath
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.smartregister.fct.engine.ModuleSetup
import org.smartregister.fct.fm.data.datasource.InAppFileSystem
import org.smartregister.fct.fm.data.datasource.MacFileSystem
import org.smartregister.fct.fm.data.datasource.UnixFileSystem
import org.smartregister.fct.fm.data.datasource.WindowsFileSystem
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.ui.viewmodel.InAppFileManagerViewModel
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel

class FileManagerModuleSetup : ModuleSetup {

    private val fileManagerModule = module(createdAtStart = true) {
        single<FileSystem> {
            val os = System.getProperty("os.name").lowercase()
            if (os.startsWith("mac os x")) {
                MacFileSystem()
            } else if (os.startsWith("windows")) {
                WindowsFileSystem()
            } else {
                UnixFileSystem()
            }
        }
        single<FileSystem>(named("inApp")) { InAppFileSystem() }
        single<SystemFileManagerViewModel> { SystemFileManagerViewModel(get()) }
        single<InAppFileManagerViewModel> { InAppFileManagerViewModel(get(named("inApp"))) }
    }

    override suspend fun setup() {
        checkAndCreateRootDir()
        GlobalContext.get().loadModules(listOf(fileManagerModule))
    }

    private fun checkAndCreateRootDir() {
        val okioFileSystem = okio.FileSystem.SYSTEM
        val rootPath = InAppFileSystem.ROOT_PATH.toPath()

        if (!okioFileSystem.exists(rootPath)) {
            okioFileSystem.createDirectory(rootPath)
        }
    }
}