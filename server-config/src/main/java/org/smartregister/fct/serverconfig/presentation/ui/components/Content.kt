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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.delay
import org.smartregister.fct.aurora.ui.components.OutlinedButton
import org.smartregister.fct.aurora.ui.components.TextButton
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigComponent

@Composable
internal fun ServerConfigComponent.Content() {

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    TextFieldItem(
                        text = username.subscribeAsState(),
                        label = "Username",
                        onValueChange = ::setUsername
                    )
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    TextFieldItem(
                        text = password.subscribeAsState(),
                        label = "Password",
                        onValueChange = ::setPassword
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = fhirBaseUrl.subscribeAsState(),
                label = "Fhir Base Url",
                onValueChange = ::setFhirBaseUrl
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = oAuthUrl.subscribeAsState(),
                label = "OAuth Url",
                onValueChange = ::setOAuthUrl
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = clientId.subscribeAsState(),
                label = "Client Id",
                onValueChange = ::setClientId
            )
            Spacer(Modifier.height(12.dp))
            TextFieldItem(
                text = authToken.subscribeAsState(),
                label = "Auth Token",
                readOnly = true,
                onValueChange = {}
            )
            Spacer(Modifier.height(12.dp))
            Row {
                OutlinedButton(
                    onClick = ::save,
                    label = "Save"
                )
                Spacer(Modifier.width(12.dp))
                TextButton(
                    label = "Authenticate",
                    onClick = ::authenticate
                )
            }
        }

        AnimatedVisibility(settingSaved.subscribeAsState().value) {
            Text("Setting Saved")
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