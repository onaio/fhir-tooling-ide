package org.smartregister.fct.engine

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.database.Database
import org.smartregister.fct.engine.domain.usecase.GetAppSetting
import org.smartregister.fct.engine.domain.usecase.UpdateAppSetting

class EngineModuleSetup : ModuleSetup {

    private val engineModule = module {
        single<GetAppSetting> { GetAppSetting(Database.getDatabase().appSettingsDaoQueries) }
        single<UpdateAppSetting> { UpdateAppSetting(Database.getDatabase().appSettingsDaoQueries) }
    }
    override fun setup() {
        GlobalContext.get().loadModules(listOf(engineModule))
    }
}