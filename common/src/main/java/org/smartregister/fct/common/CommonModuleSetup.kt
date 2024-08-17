package org.smartregister.fct.common

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.common.domain.mdoule.ModuleSetup
import org.smartregister.fct.common.domain.usecase.GetAppSetting
import org.smartregister.fct.common.domain.usecase.UpdateAppSetting
import org.smartregister.fct.common.presentation.viewmodel.AppSettingViewModel
import org.smartregister.fct.database.Database
import org.smartregister.fct.logger.FCTLogger
import sqldelight.AppSettingsDaoQueries

class CommonModuleSetup : ModuleSetup {

    private val commonModule = module(createdAtStart = true) {
        single<AppSettingsDaoQueries> { Database.getDatabase().appSettingsDaoQueries }
        single<GetAppSetting> { GetAppSetting(get()) }
        single<UpdateAppSetting> { UpdateAppSetting(get()) }
        single<AppSettingViewModel> { AppSettingViewModel() }
    }

    override suspend fun setup() {
        FCTLogger.d("Loading... Common Module")
        GlobalContext.get().loadModules(listOf(commonModule))
        FCTLogger.d("Common Module Loaded")
    }
}