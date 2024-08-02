package org.smartregister.fct.configs.domain.model


abstract class JsonConfiguration : java.io.Serializable  {
    open lateinit var appId: String
    open lateinit var configType: String
}