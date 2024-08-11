package org.smartregister.fct.fm

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.engine.ModuleSetup
import org.smartregister.fct.fm.data.datasource.MacFileSystem
import org.smartregister.fct.fm.data.datasource.UnixFileSystem
import org.smartregister.fct.fm.data.datasource.WindowsFileSystem
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel

class FileManagerModuleSetup : ModuleSetup {

    private val fileManagerModule = module {
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
        single<SystemFileManagerViewModel> { SystemFileManagerViewModel(get()) }
    }

    override fun setup() {
        GlobalContext.get().loadModules(listOf(fileManagerModule))
    }
}