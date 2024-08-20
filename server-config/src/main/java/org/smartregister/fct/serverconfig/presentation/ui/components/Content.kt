package org.smartregister.fct.serverconfig.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import fct.server_config.generated.resources.Res
import fct.server_config.generated.resources.auth_token
import fct.server_config.generated.resources.client_id
import fct.server_config.generated.resources.client_secret
import fct.server_config.generated.resources.config_verified
import fct.server_config.generated.resources.description
import fct.server_config.generated.resources.error
import fct.server_config.generated.resources.failed
import fct.server_config.generated.resources.fhir_base_url
import fct.server_config.generated.resources.oauth_url
import fct.server_config.generated.resources.password
import fct.server_config.generated.resources.save
import fct.server_config.generated.resources.setting_saved
import fct.server_config.generated.resources.success
import fct.server_config.generated.resources.username
import fct.server_config.generated.resources.verify
import kotlinx.coroutines.delay
import org.smartregister.fct.apiclient.domain.model.AuthResponse
import org.smartregister.fct.aurora.ui.components.Button
import org.smartregister.fct.aurora.ui.components.OutlinedButton
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.serverconfig.domain.model.VerifyConfigState
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigComponent
import org.smartregister.fct.serverconfig.util.asString

context (ServerConfigComponent)
@Composable
internal fun Content() {

    val verifyConfigState by verifyConfigState.subscribeAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    TextFieldItem(
                        text = username.subscribeAsState(),
                        label = Res.string.username.asString(),
                        onValueChange = ::setUsername
                    )
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    TextFieldItem(
                        text = password.subscribeAsState(),
                        label = Res.string.password.asString(),
                        onValueChange = ::setPassword
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = fhirBaseUrl.subscribeAsState(),
                label = Res.string.fhir_base_url.asString(),
                onValueChange = ::setFhirBaseUrl
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = oAuthUrl.subscribeAsState(),
                label = Res.string.oauth_url.asString(),
                onValueChange = ::setOAuthUrl
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = clientId.subscribeAsState(),
                label = Res.string.client_id.asString(),
                onValueChange = ::setClientId
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = clientSecret.subscribeAsState(),
                label = Res.string.client_secret.asString(),
                onValueChange = ::setClientSecret
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = authToken.subscribeAsState(),
                label = Res.string.auth_token.asString(),
                readOnly = true,
                onValueChange = {}
            )
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = ::save,
                    label = Res.string.save.asString()
                )
                Spacer(Modifier.width(12.dp))

                when (val state = verifyConfigState) {
                    is VerifyConfigState.Idle -> {
                        OutlinedButton(
                            label = Res.string.verify.asString(),
                            onClick = ::verifyAuthConfig
                        )
                    }

                    is VerifyConfigState.Authenticating -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }

                    is VerifyConfigState.Result -> {
                        ShowAuthResponseDialog(state.authResponse)
                    }
                }
            }
        }

        AnimatedVisibility(settingSaved.subscribeAsState().value) {
            Text(Res.string.setting_saved.asString())
            LaunchedEffect(null) {
                delay(1500)
                settingSaved.value = false
            }
        }
    }
}

@Composable
internal fun TextFieldItem(
    modifier: Modifier = Modifier,
    text: State<String>,
    label: String,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit
) {

    Row {
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = text.value,
            onValueChange = onValueChange,
            label = {
                Text(text = label)
            },
            readOnly = readOnly,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),

                )
        )
    }
}

context (ServerConfigComponent)
@Composable
fun ShowAuthResponseDialog(authResponse: AuthResponse) {

    val title =
        if (authResponse is AuthResponse.Success) Res.string.success.asString()
        else Res.string.failed.asString()

    val dialogType =
        if (authResponse is AuthResponse.Success) DialogType.Default else DialogType.Error

    val responseDialog = rememberAlertDialog<Unit>(
        title = title,
        dialogType = dialogType,
        onDismiss = {
            verifyConfigState.value = VerifyConfigState.Idle
        }
    ) { _, _ ->
        when (authResponse) {
            is AuthResponse.Success -> {
                Text(Res.string.config_verified.asString().format(serverConfig.title))
            }

            is AuthResponse.Failed -> {
                Text(Res.string.error.asString(), style = TextStyle(fontWeight = FontWeight.Bold))
                Text(authResponse.error)

                authResponse.description?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        Res.string.description.asString(),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Text(it)
                }
            }
        }
    }

    responseDialog.show()
}