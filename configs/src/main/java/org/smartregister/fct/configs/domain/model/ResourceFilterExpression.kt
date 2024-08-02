package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable
import org.hl7.fhir.r4.model.ResourceType

@Serializable
data class ResourceFilterExpression(
  val conditionalFhirPathExpressions: List<String>,
  val matchAll: Boolean = true,
  val resourceType: ResourceType? = null,
) 