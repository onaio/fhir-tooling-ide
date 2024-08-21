package org.smartregister.fct.apiclient.data.datasource

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.json.json
import org.hl7.fhir.r4.model.OperationOutcome
import org.smartregister.fct.apiclient.data.deserializer.OperationOutcomeTypeAdapter
import org.smartregister.fct.apiclient.domain.datasource.ApiClientDataSource
import org.smartregister.fct.apiclient.domain.model.AuthRequest
import org.smartregister.fct.apiclient.domain.model.AuthResponse
import org.smartregister.fct.apiclient.domain.model.UploadResourceRequest
import org.smartregister.fct.apiclient.domain.model.UploadResponse
import org.smartregister.fct.apiclient.util.asOperationOutcome
import org.smartregister.fct.logger.FCTLogger

internal class KtorApiClientDataSource(private val gson: Gson) : ApiClientDataSource {

    override suspend fun auth(request: AuthRequest): AuthResponse {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        return try {
            val response = client.submitForm(
                url = request.oAuthUrl.also { FCTLogger.d("oAuthUrl = ${request.oAuthUrl}") },
                formParameters = parameters {
                    append("grant_type", "password")
                    append("scope", "openid")
                    append("username", request.username)
                    append("password", request.password)
                    append("client_id", request.clientId)
                    append("client_secret", request.clientSecret)
                }.also {
                    FCTLogger.d(it.toString())
                }
            )

            if (response.status == HttpStatusCode.OK) {
                val success: AuthResponse.Success = response.body()
                FCTLogger.i(success.asPrettyJson())
                success
            } else {
                val failed: AuthResponse.Failed = response.body()
                FCTLogger.e(failed.asPrettyJson())
                failed
            }

        } catch (ex: Exception) {
            FCTLogger.e(ex)
            AuthResponse.Failed(
                error = ex.message ?: "Unknown Error"
            )
        } finally {
            client.close()
        }
    }

    override suspend fun upload(
        request: UploadResourceRequest
    ): UploadResponse {
        val client = HttpClient(CIO) {

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(request.accessToken, "").also {
                            FCTLogger.d("AuthToken -> ${it.accessToken}")
                        }
                    }
                }
            }

            install(ContentNegotiation) {
                gson {
                    registerTypeAdapter(
                        OperationOutcome::class.java,
                        OperationOutcomeTypeAdapter()
                    )
                }
            }
        }

        return try {
            val response = client.request(request.url) {
                method = HttpMethod.Put
                url {
                    appendPathSegments(request.resourceType, request.id).also {
                        FCTLogger.d("PUT -> $it")
                    }
                }
                contentType(ContentType.Application.Json)
                setBody(request.body)
            }

            when (response.status) {
                HttpStatusCode.OK -> UploadResponse.Success
                HttpStatusCode.Unauthorized -> UploadResponse.UnAuthorized
                else -> {
                    val outcome: OperationOutcome = response.body()
                    FCTLogger.e(response.bodyAsText())
                    UploadResponse.Failed(outcome)
                }
            }
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            UploadResponse.Failed(ex.asOperationOutcome())

        } finally {
            client.close()
        }

    }

    private fun <T> T.asPrettyJson(): String {
        return gson.toJson(this)
    }
}