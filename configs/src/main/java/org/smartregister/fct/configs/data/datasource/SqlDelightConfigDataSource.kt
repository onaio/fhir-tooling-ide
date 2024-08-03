package org.smartregister.fct.configs.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import org.smartregister.fct.configs.domain.datasource.ConfigDataSource
import org.smartregister.fct.configs.domain.model.ConfigType
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import org.smartregister.fct.configs.domain.model.RegisterConfiguration
import org.smartregister.fct.configs.util.extension.decodeJson
import org.smartregister.fct.configs.util.extension.decompress
import sqldelight.Config
import sqldelight.ConfigQueries

class SqlDelightConfigDataSource(private val configQueries: ConfigQueries) : ConfigDataSource {

    override fun getAllConfigs(): Flow<List<ConfigWrapper>> {
        return configQueries.selectAll().asFlow().mapToList(Dispatchers.IO).map {
            it.map { config ->
                val jsonString = config.body.decompress()
                val json = JSONObject(jsonString)

                val configuration = when (json.getString("configType")) {
                    ConfigType.Register.name -> jsonString.decodeJson<RegisterConfiguration>()
                    else -> throw IllegalStateException("${json.getString("configType")} is unknown config type")
                }
                ConfigWrapper(
                    config.config_id,
                    config.config_title,
                    configuration
                )
            }
        }
    }

    override fun insertConfig(config: Config) {
        configQueries.insertConfig(config.config_id, config.config_title, config.body)
    }

    override fun updateConfig(uuid: String, json: String) {
        configQueries.updateById(json, uuid)
    }

    override fun deleteConfig(uuid: String) {
        configQueries.deleteById(uuid)
    }
}
