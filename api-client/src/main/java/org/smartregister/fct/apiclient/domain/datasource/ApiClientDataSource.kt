package org.smartregister.fct.apiclient.domain.datasource

import org.smartregister.fct.apiclient.domain.model.AuthResponse

interface ApiClientDataSource {

    suspend fun auth(
        url: String,
        clientId: String,
        clientSecret: String,
        username: String,
        password: String
    ) : AuthResponse
}