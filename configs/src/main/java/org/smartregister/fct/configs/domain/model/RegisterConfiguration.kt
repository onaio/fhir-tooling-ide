package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterConfiguration(
    override var appId: String,
    override var configType: String = ConfigType.Register.name,
    val id: String,
    val registerTitle: String? = null,
    var fhirResource: FhirResourceConfig? = null,
    var secondaryResources: MutableList<FhirResourceConfig>? = null,
    val searchBar: RegisterContentConfig? = null,
    val registerCard: RegisterCardConfig? = null,
    val fabActions: List<NavigationMenuConfig>? = null,
    val noResults: NoResultsConfig? = null,
    val pageSize: Int? = null,
    val activeResourceFilters: List<ActiveResourceFilterConfig>? = null,
    val configRules: List<RuleConfig>? = null,
    val registerFilter: RegisterFilterConfig? = null,
    val filterDataByRelatedEntityLocation: Boolean? = null,
    val topScreenSection: TopScreenSectionConfig? = null,
) : JsonConfiguration()
