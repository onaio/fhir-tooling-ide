package org.smartregister.fct.device_database.ui.components

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.smartregister.fct.device_database.data.persistence.DeviceDBConfigPersistence
import org.smartregister.fct.engine.util.componentScope

class QueryResultComponent(
    componentContext: ComponentContext,
    private val persistedQuery: String,
    val result: Result<JSONObject>,
) : ComponentContext by componentContext {

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _query = MutableStateFlow(persistedQuery)
    val query: StateFlow<String> = _query


}