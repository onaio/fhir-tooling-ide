package org.smartregister.fct.fm

import okio.Path.Companion.toPath
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.smartregister.fct.engine.domain.mdoule.ModuleSetup
import org.smartregister.fct.fm.data.FileHandler
import org.smartregister.fct.fm.data.datasource.InAppFileSystem
import org.smartregister.fct.fm.data.datasource.MacFileSystem
import org.smartregister.fct.fm.data.datasource.UnixFileSystem
import org.smartregister.fct.fm.data.datasource.WindowsFileSystem
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.ui.viewmodel.InAppFileManagerViewModel
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel
import org.smartregister.fct.logger.FCTLogger

class FileManagerModuleSetup : ModuleSetup {

    private val fileManagerModule = module {
        single<FileSystem>(createdAtStart = true) {
            val os = System.getProperty("os.name").lowercase()
            if (os.startsWith("mac os x")) {
                MacFileSystem()
            } else if (os.startsWith("windows")) {
                WindowsFileSystem()
            } else {
                UnixFileSystem()
            }
        }
        single<FileSystem>(named("inApp"), true) { InAppFileSystem() }
        single<InAppFileManagerViewModel> { InAppFileManagerViewModel(get(named("inApp"))) }
        single<SystemFileManagerViewModel> { SystemFileManagerViewModel(get()) }
        single<FileHandler> { FileHandler(get<SystemFileManagerViewModel>(), get<InAppFileManagerViewModel>() )}
    }

    override suspend fun setup() {
        FCTLogger.d("Loading... File Manager Module")
        checkAndCreateRootDir()
        GlobalContext.get().loadModules(listOf(fileManagerModule))
        FCTLogger.d("File Manager Module Loaded")
    }

    private fun checkAndCreateRootDir() {
        val okioFileSystem = okio.FileSystem.SYSTEM
        val rootPath = InAppFileSystem.ROOT_PATH.toPath()

        if (!okioFileSystem.exists(rootPath)) {
            okioFileSystem.createDirectory(rootPath)
        }
    }
}