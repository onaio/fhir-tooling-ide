package org.smartregister.fct.engine

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.database.Database
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.domain.usecase.GetAppSetting
import org.smartregister.fct.engine.domain.usecase.UpdateAppSetting
import org.smartregister.fct.engine.setup.ModuleSetup
import org.smartregister.fct.logger.FCTLogger
import sqldelight.AppSettingsDaoQueries

class EngineModuleSetup : ModuleSetup {

    private val engineModule = module(createdAtStart = true) {
        single<AppSettingsDaoQueries> {
            Database.getDatabase().appSettingsDaoQueries
        }
        single<AppSettingManager> {
            AppSettingManager(
                GetAppSetting(get()),
                UpdateAppSetting(get())
            )
        }
    }

    override suspend fun setup() {
        FCTLogger.d("Loading... Engine Module")
        GlobalContext.get().loadModules(listOf(engineModule))
        FCTLogger.d("Engine Module Loaded")
    }
}