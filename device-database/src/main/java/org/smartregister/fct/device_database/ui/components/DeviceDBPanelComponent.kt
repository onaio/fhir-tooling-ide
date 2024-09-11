package org.smartregister.fct.device_database.ui.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.adb.domain.usecase.DeviceManager
import org.smartregister.fct.device_database.data.persistence.DeviceDBConfigPersistence
import org.smartregister.fct.device_database.domain.DBInfo
import org.smartregister.fct.device_database.domain.TableInfo
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.logger.FCTLogger

internal class DeviceDBPanelComponent(componentContext: ComponentContext) : QueryDependency, ComponentContext by componentContext {

    private var selectedDBInfo = DeviceDBConfigPersistence.sidePanelDBInfo

    private var _listOfTables = MutableStateFlow(DeviceDBConfigPersistence.tablesMap[selectedDBInfo.name] ?: listOf())
    val listOfTables: StateFlow<List<TableInfo>> = _listOfTables

    private var _loadingTables = MutableStateFlow(false)
    val loadingTables: StateFlow<Boolean> = _loadingTables

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        if (_listOfTables.value.isEmpty()) {
            getRequiredParam (false) { selectedDBInfo, activeDevice, selectedPackage ->
                fetchTables(selectedDBInfo, activeDevice, selectedPackage.packageId)
            }
        }
    }

    fun reFetchTables() {
        if (_loadingTables.value) return
        getRequiredParam { selectedDBInfo, activeDevice, selectedPackage ->
            fetchTables(selectedDBInfo, activeDevice, selectedPackage.packageId)
        }
    }

    fun updateDatabase(dbInfo: DBInfo) {
        if (selectedDBInfo.name == dbInfo.name) return
        componentScope.launch {
            selectedDBInfo = dbInfo
            DeviceDBConfigPersistence.sidePanelDBInfo = dbInfo
            val existingTables = DeviceDBConfigPersistence.tablesMap[dbInfo.name]!!
            if (existingTables.isEmpty()) {
                reFetchTables()
            } else {
               _listOfTables.emit(existingTables)
           }
        }
    }

    private fun fetchTables(dbInfo: DBInfo, device: Device, packageId: String) {
        componentScope.launch {
            _loadingTables.emit(true)

            val result = device.runAppDBQuery(
                database = dbInfo.name,
                query = "SELECT * FROM sqlite_schema WHERE type='table' AND name NOT LIKE 'sqlite_%'",
                packageId = packageId
            )

            _loadingTables.emit(false)
            if (result.isSuccess) {
                _listOfTables.emit(parseResultToTables(result))
                DeviceDBConfigPersistence.tablesMap[dbInfo.name] = _listOfTables.value
            } else {
                showError(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    override fun getRequiredParam(showErrors: Boolean, info: (DBInfo, Device, PackageInfo) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val activeDevice = DeviceManager.getActiveDevice()
            val selectedPackage = DeviceManager.getActivePackage().value

            if (activeDevice == null) {
                if (showErrors) showError("No Device Selected")
                return@launch
            } else if (selectedPackage == null) {
                if (showErrors) showError("No package selected")
                return@launch
            }
            info(selectedDBInfo, activeDevice, selectedPackage)
        }
    }

    private suspend fun showError(message: String) {
        _error.emit(message)
        delay(200)
        _error.emit(null)
    }

    private fun parseResultToTables(result: Result<JSONObject>): List<TableInfo> {
        return try {
            val jsonArray = result.getOrThrow().getJSONArray("data")
            jsonArray.map {
                TableInfo(
                    name = (it as JSONObject).getString("tbl_name")
                )
            }
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            listOf()
        }

    }
}