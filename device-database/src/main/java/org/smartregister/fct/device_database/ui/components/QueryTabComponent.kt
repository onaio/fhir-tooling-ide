package org.smartregister.fct.device_database.ui.components

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Resource
import org.json.JSONObject
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.adb.domain.usecase.DeviceManager
import org.smartregister.fct.device_database.data.controller.QueryResultDataController
import org.smartregister.fct.device_database.data.persistence.DeviceDBConfigPersistence
import org.smartregister.fct.device_database.domain.model.QueryRequest
import org.smartregister.fct.device_database.domain.model.QueryResponse
import org.smartregister.fct.engine.util.compactJson
import org.smartregister.fct.engine.util.decodeResourceFromString

class QueryTabComponent(
    componentContext: ComponentContext,
) : TabComponent(componentContext), QueryDependency {

    var selectedDBInfo = DeviceDBConfigPersistence.listOfDB[0]

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _query = MutableStateFlow(TextFieldValue("select * from ResourceEntity"))
    val query: StateFlow<TextFieldValue> = _query

    private val _queryResultDataController = MutableStateFlow<QueryResultDataController?>(null)
    internal val queryResultDataController: StateFlow<QueryResultDataController?> = _queryResultDataController

    private val limit = 50

    fun updateTextField(textFieldValue: TextFieldValue) {
        CoroutineScope(Dispatchers.Default).launch {
            _query.emit(textFieldValue)
        }
    }

    fun runQuery() {
        if (_query.value.text.trim().isEmpty() || _loading.value) return

        getRequiredParam { device, pkg ->
            CoroutineScope(Dispatchers.IO).launch {
                _loading.emit(true)
                val result = device.runAppDBQuery(
                    packageId = pkg.packageId,
                    requestJson = QueryRequest(
                        database = selectedDBInfo.name,
                        query = _query.value.text,
                        limit = limit
                    ).asJSONString()
                )

                val queryResponse = QueryResponse.build(result)

                _loading.emit(false)
                _queryResultDataController.emit(
                    QueryResultDataController(
                        initialLimit = limit,
                        initialQuery = _query.value.text,
                        queryResponse = queryResponse,
                        database = selectedDBInfo.name,
                        componentContext = this@QueryTabComponent
                    )
                )
            }
        }
    }

    suspend fun updateRecordByResourceId(
        serializedResource: String,
        database: String = DeviceDBConfigPersistence.listOfDB[0].name,
    ): Result<JSONObject> {

        val minifyResource = try {
            serializedResource.compactJson()
        } catch (ex: Exception) {
            return Result.failure(ex)
        }

        val resourceId = try {
            minifyResource.decodeResourceFromString<Resource>().idPart
        } catch (ex: Exception) {
            return Result.failure(ex)
        }

        val activeDevice = DeviceManager.getActiveDevice()
        val selectedPackage = DeviceManager.getActivePackage().value

        if (activeDevice == null) {
            return Result.failure(IllegalStateException("No Device Selected"))
        } else if (selectedPackage == null) {
            return Result.failure(IllegalStateException("No package selected"))
        }

        val query =
            "UPDATE ResourceEntity SET serializedResource='${minifyResource.replace("'","''")}' WHERE resourceId='$resourceId'"

        return activeDevice.runAppDBQuery(
            packageId = selectedPackage.packageId,
            requestJson = QueryRequest(
                database = database,
                query = query,
                limit = 1
            ).asJSONString()
        )
    }

    override fun getRequiredParam(
        showErrors: Boolean,
        info: (Device, PackageInfo) -> Unit
    ) {
        if (componentContext is QueryDependency) {
            componentContext.getRequiredParam(showErrors, info)
        }
    }
}