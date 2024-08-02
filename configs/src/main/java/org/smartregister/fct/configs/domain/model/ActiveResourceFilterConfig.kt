package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable
import org.hl7.fhir.r4.model.ResourceType

@Serializable
data class ActiveResourceFilterConfig(val resourceType: ResourceType, val active: Boolean)
