package org.smartregister.fct.engine.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Config {
    data object DataSpecification : Config()
    data object StructureMap : Config()
    data object FileManager : Config()
}