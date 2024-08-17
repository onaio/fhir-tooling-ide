package org.smartregister.fct.configs.domain.model

import org.smartregister.fct.common.util.encodeJson


data class ConfigWrapper(
    val uuid: String,
    val title: String,
    val config: JsonConfiguration
) {
    fun<T: JsonConfiguration> get(): T {
        return config as T
    }

    fun encodeConfig(): String {
        return when(config) {
            is RegisterConfiguration -> get<RegisterConfiguration>().encodeJson()
            else -> throw IllegalStateException("config type ${config.configType} is not valid")
        }
    }
}