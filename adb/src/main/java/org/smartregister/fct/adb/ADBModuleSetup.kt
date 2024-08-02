package org.smartregister.fct.adb

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.adb.data.commands.GetAllDevicesCommand
import org.smartregister.fct.adb.data.commands.GetDeviceInfoCommand
import org.smartregister.fct.adb.data.controller.ADBController
import org.smartregister.fct.adb.data.shell.KScriptShellProgram
import org.smartregister.fct.adb.domain.program.ShellProgram
import org.smartregister.fct.engine.ModuleSetup
import org.smartregister.fct.logcat.FCTLogger
import org.smartregister.fct.logcat.domain.model.Log
import org.smartregister.fct.logcat.domain.model.LogFilter

class ADBModuleSetup : ModuleSetup {

    private val adbModule = module {
        single<ShellProgram> { KScriptShellProgram() }
        single<ADBController> { ADBController(get()) }
    }

    override fun setup() {
        GlobalContext.get().loadModules(listOf(adbModule))
        FCTLogger.addFilter(ADBCommandFilter())
    }

    private class ADBCommandFilter : LogFilter {

        override fun isLoggable(log: Log): Boolean {
            return log.tag.let { it != GetAllDevicesCommand::class.java.simpleName && it != GetDeviceInfoCommand::class.java.simpleName }
        }

    }
}