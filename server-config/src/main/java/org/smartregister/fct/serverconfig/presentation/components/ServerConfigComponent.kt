package org.smartregister.fct.serverconfig.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.apiclient.data.datasource.KtorApiClientDataSource
import org.smartregister.fct.apiclient.data.repository.ApiClientRepository
import org.smartregister.fct.apiclient.domain.model.AuthResponse
import org.smartregister.fct.common.data.manager.AppSettingManager
import org.smartregister.fct.common.domain.model.AppSetting
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.common.util.componentScope
import org.smartregister.fct.serverconfig.domain.model.VerifyConfigState

class ServerConfigComponent(
    componentContext: ComponentContext,
    val serverConfig: ServerConfig,
) : KoinComponent, ComponentContext by componentContext {

    private val apiClientRepository: ApiClientRepository by inject()
    private val appSettingManager: AppSettingManager by inject()
    private lateinit var appSetting: AppSetting

    init {
        componentScope.launch {
            appSettingManager.getAppSettingFlow().collectLatest {
                appSetting = it
            }
        }
    }

    private val _fhirBaseUrl = MutableValue(serverConfig.fhirBaseUrl)
    val fhirBaseUrl: Value<String> = _fhirBaseUrl

    private val _oAuthUrl = MutableValue(serverConfig.oAuthUrl)
    val oAuthUrl: Value<String> = _oAuthUrl

    private val _clientId = MutableValue(serverConfig.clientId)
    val clientId: Value<String> = _clientId

    private val _clientSecret = MutableValue(serverConfig.clientSecret)
    val clientSecret: Value<String> = _clientSecret

    private val _username = MutableValue(serverConfig.username)
    val username: Value<String> = _username

    private val _password = MutableValue(serverConfig.password)
    val password: Value<String> = _password

    private val _authToken = MutableValue(serverConfig.authToken)
    val authToken: Value<String> = _authToken

    val settingSaved = MutableValue(false)
    val verifyConfigState = MutableValue<VerifyConfigState>(VerifyConfigState.Idle)

    fun setFhirBaseUrl(text: String) {
        _fhirBaseUrl.value = text
    }

    fun setOAuthUrl(text: String) {
        _oAuthUrl.value = text
    }

    fun setClientId(text: String) {
        _clientId.value = text
    }

    fun setClientSecret(text: String) {
        _clientSecret.value = text
    }

    fun setUsername(text: String) {
        _username.value = text
    }

    fun setPassword(text: String) {
        _password.value = text
    }

    fun save() {
        componentScope.launch {
            val updatedConfig = serverConfig.copy(
                fhirBaseUrl = _fhirBaseUrl.value,
                oAuthUrl =  _oAuthUrl.value,
                clientId = _clientId.value,
                clientSecret = _clientSecret.value,
                username = _username.value,
                password = _password.value,
                authToken = _authToken.value
            )

            appSetting
                .serverConfigs
                .map {
                    if (it.id == updatedConfig.id) updatedConfig
                    else it
                }.let {
                    appSetting.copy(serverConfigs = it)
                }.run {
                    appSettingManager.setAndUpdate(this)
                }.also {
                    settingSaved.value = true
                }
        }
    }

    fun verifyAuthConfig() {
        componentScope.launch {

            verifyConfigState.value = VerifyConfigState.Authenticating

            val response = apiClientRepository.auth(
                url = oAuthUrl.value,
                clientId = clientId.value,
                clientSecret = clientSecret.value,
                username = username.value,
                password = password.value
            )

            if (response is AuthResponse.Success) {
                _authToken.value = response.accessToken
            }

            verifyConfigState.value = VerifyConfigState.Result(response)
        }
    }
}