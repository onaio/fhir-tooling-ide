package org.smartregister.fct.configs.data.viewmodel

import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.configs.data.repository.ConfigRepository
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import org.smartregister.fct.engine.util.uuid
import sqldelight.Config

class ConfigManagerViewModel :  KoinComponent {

    private val configRepository: ConfigRepository by inject()

    var isFirstTimeComposition = true

    fun getAllConfigs(): Flow<List<ConfigWrapper>> {
        return configRepository.getAllConfigs()
    }

    fun insertConfig(title: String, json: String) {
        configRepository.insertConfig(Config(uuid(), title, json))
    }

    fun updateConfig(configWrapper: ConfigWrapper) {

    }

    fun deleteConfig(uuid: String) {
        configRepository.deleteConfig(uuid)
    }
}