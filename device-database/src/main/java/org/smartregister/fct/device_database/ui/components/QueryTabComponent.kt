package org.smartregister.fct.device_database.ui.components

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.device_database.data.persistence.DeviceDBConfigPersistence
import org.smartregister.fct.engine.util.componentScope

class QueryTabComponent(
    componentContext: ComponentContext,
) : TabComponent(componentContext) {

    var selectedDBInfo = DeviceDBConfigPersistence.sidePanelDBInfo

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _query = MutableStateFlow(TextFieldValue())
    val query: StateFlow<TextFieldValue> = _query

    private val _queryResultComponent = MutableStateFlow<QueryResultComponent?>(null)
    val queryResultComponent: StateFlow<QueryResultComponent?> = _queryResultComponent

    fun updateTextField(textFieldValue: TextFieldValue) {
        CoroutineScope(Dispatchers.Default).launch {
            _query.emit(textFieldValue)
        }
    }

    fun runQuery() {
        if (_query.value.text.trim().isEmpty() || _loading.value) return

        if (componentContext is QueryDependency) {
            componentContext.getRequiredParam { _, device, pkg ->
                CoroutineScope(Dispatchers.IO).launch {
                    _loading.emit(true)
                    val result = device.runAppDBQuery(
                        database = selectedDBInfo.name,
                        query = _query.value.text,
                        packageId = pkg.packageId
                    )

                    _loading.emit(false)
                    _queryResultComponent.emit(
                        QueryResultComponent(
                            componentContext = componentContext,
                            persistedQuery = _query.value.text,
                            result = result
                        )
                    )
                }
            }
        }
    }
}