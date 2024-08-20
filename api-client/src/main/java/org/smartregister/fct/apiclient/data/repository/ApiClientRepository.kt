package org.smartregister.fct.apiclient.data.repository

import org.smartregister.fct.apiclient.domain.datasource.ApiClientDataSource
import org.smartregister.fct.apiclient.domain.model.AuthResponse

class ApiClientRepository(private val dataSource: ApiClientDataSource)  {

    suspend fun auth(
        url: String,
        clientId: String,
        clientSecret: String,
        username: String,
        password: String
    ) : AuthResponse = dataSource.auth(
        url = url,
        clientId = clientId,
        clientSecret = clientSecret,
        username = username,
        password = password
    )
}