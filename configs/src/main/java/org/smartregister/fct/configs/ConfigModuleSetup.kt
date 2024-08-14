package org.smartregister.fct.configs

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.configs.data.repository.ConfigRepository
import org.smartregister.fct.configs.domain.datasource.ConfigDataSource
import org.smartregister.fct.configs.data.datasource.SqlDelightConfigDataSource
import org.smartregister.fct.database.Database
import org.smartregister.fct.engine.domain.mdoule.ModuleSetup
import org.smartregister.fct.logger.FCTLogger

class ConfigModuleSetup : ModuleSetup {

    private val configModule = module {
        single<ConfigDataSource> {
            SqlDelightConfigDataSource(Database.getDatabase().configQueries)
        }
        single<ConfigRepository> { ConfigRepository(get()) }
    }

    override suspend fun setup() {
        FCTLogger.d("Loading... Config Module")
        GlobalContext.get().loadModules(listOf(configModule))
        FCTLogger.d("Config Module Loaded")
    }
}