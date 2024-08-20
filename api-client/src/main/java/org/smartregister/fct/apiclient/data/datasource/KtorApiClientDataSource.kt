package org.smartregister.fct.apiclient.data.datasource

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import org.smartregister.fct.apiclient.domain.datasource.ApiClientDataSource
import org.smartregister.fct.apiclient.domain.model.AuthResponse
import org.smartregister.fct.logger.FCTLogger


class KtorApiClientDataSource(private val gson: Gson) : ApiClientDataSource {

    override suspend fun auth(
        url: String,
        clientId: String,
        clientSecret: String,
        username: String,
        password: String
    ): AuthResponse {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.submitForm(
            url = url.also { FCTLogger.d("oAuthUrl = $url") },
            formParameters = parameters {
                append("grant_type", "password")
                append("scope", "openid")
                append("username", username)
                append("password", password)
                append("client_id", clientId)
                append("client_secret", clientSecret)
            }.also {
                FCTLogger.d(it.toString())
            }
        )

        client.close()

        return if (response.status == HttpStatusCode.OK) {
            val success: AuthResponse.Success = response.body()
            FCTLogger.i(success.asPrettyJson())
            success
        } else {
            val failed: AuthResponse.Failed = response.body()
            FCTLogger.e(failed.asPrettyJson())
            failed
        }
    }

    private fun AuthResponse.asPrettyJson(): String {
        return gson.toJson(this)
    }
}