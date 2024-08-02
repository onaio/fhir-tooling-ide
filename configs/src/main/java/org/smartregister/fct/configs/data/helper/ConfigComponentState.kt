package org.smartregister.fct.configs.data.helper

import org.smartregister.fct.configs.data.viewmodel.ComponentNavigator
import org.smartregister.fct.configs.domain.model.FhirResourceConfig
import org.smartregister.fct.configs.domain.model.ResourceConfig


sealed class ConfigComponentState(val title: String, val config: Any?, val navigator: ComponentNavigator) {

    class FhirResource(title: String = "Fhir Resource", val fhirResourceConfig: FhirResourceConfig?, navigator: ComponentNavigator) :
        ConfigComponentState(title, fhirResourceConfig, navigator)

    class BaseResource(title: String = "Base Resource", val baseResource: ResourceConfig?, navigator: ComponentNavigator) :
        ConfigComponentState(title, baseResource, navigator)

    class SecondaryResources(title: String = "Secondary Resource", val secondaryResources: MutableList<FhirResourceConfig>?, navigator: ComponentNavigator) :
        ConfigComponentState(title, secondaryResources, navigator)
}