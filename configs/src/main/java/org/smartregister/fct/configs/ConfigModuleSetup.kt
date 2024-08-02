package org.smartregister.fct.configs

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.smartregister.fct.configs.data.repository.ConfigRepository
import org.smartregister.fct.configs.data.source.ConfigDataSource
import org.smartregister.fct.configs.data.source.SqlDelightConfigDataSource
import org.smartregister.fct.database.Database
import org.smartregister.fct.engine.ModuleSetup

class ConfigModuleSetup : ModuleSetup {

    private val configModule = module {
        single<ConfigDataSource> {
            SqlDelightConfigDataSource(Database.getDatabase().configQueries)
        }
        single<ConfigRepository> { ConfigRepository(get()) }
    }

    override fun setup() {
        startKoin {
            modules(configModule)
        }
    }
}