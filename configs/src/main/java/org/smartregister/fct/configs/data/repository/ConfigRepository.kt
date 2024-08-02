package org.smartregister.fct.configs.data.repository

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.configs.data.source.ConfigDataSource
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import sqldelight.Config

class ConfigRepository(private val configDataSource: ConfigDataSource) {

    fun getAllConfigs(): Flow<List<ConfigWrapper>> {
        return configDataSource.getAllConfigs()
    }

    fun insertConfig(config: Config) {
        configDataSource.insertConfig(config)
    }

    fun updateConfig(uuid: String, json: String) {
        configDataSource.updateConfig(uuid, json)
    }

    fun deleteConfig(uuid: String) {
        configDataSource.deleteConfig(uuid)
    }
}