package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable

/**
 * @property resourceConfig The configuration for FHIR resource to be loaded
 * @property parentIdFhirPathExpression FhirPath expression for extracting the ID for the parent
 *   resource
 * @property contentFhirPathExpression FhirPath expression for extracting the content displayed on
 *   the multi select widget e.g. the name of the Location in a Location hierarchy
 * @property rootNodeFhirPathExpression A key value pair containing a FHIRPath expression for
 *   extracting the value used to identify if the current resource is Root. The key is the FHIRPath
 *   expression while value is the content to compare against.
 */

@Serializable
data class MultiSelectViewConfig(
    val resourceConfig: FhirResourceConfig,
    val parentIdFhirPathExpression: String,
    val contentFhirPathExpression: String,
    val rootNodeFhirPathExpression: KeyValueConfig,
)
