package org.smartregister.fct.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
    val id: String,
    val title: String,
    val fhirBaseUrl: String = "",
    val oAuthUrl: String = "",
    val clientId: String = "",
    val username: String = "",
    val password: String = "",
    val authToken: String = "",
)