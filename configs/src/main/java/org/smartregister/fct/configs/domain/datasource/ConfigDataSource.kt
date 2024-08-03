package org.smartregister.fct.configs.domain.datasource

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import sqldelight.Config

interface ConfigDataSource {

    fun getAllConfigs(): Flow<List<ConfigWrapper>>
    fun insertConfig(config: Config)
    fun updateConfig(uuid: String, json: String)
    fun deleteConfig(uuid: String)
}