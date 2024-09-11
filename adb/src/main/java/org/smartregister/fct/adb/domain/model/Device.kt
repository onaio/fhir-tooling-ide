package org.smartregister.fct.adb.domain.model

import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.adb.data.commands.AppDatabaseQueryCommand
import org.smartregister.fct.adb.data.commands.GetAllPackagesCommand
import org.smartregister.fct.adb.data.controller.ADBController

class Device internal constructor(internal val deviceId: String) : KoinComponent {

    private val controller: ADBController by inject()
    internal lateinit var deviceInfo: DeviceInfo

    fun getDeviceInfo(): DeviceInfo = deviceInfo

    suspend fun getAllPackages(filter: List<String>): Result<List<PackageInfo>> {
        return controller.executeCommand(GetAllPackagesCommand(filter), deviceId = deviceId)
    }

    suspend fun runAppDBQuery(database: String, query: String, packageId: String): Result<JSONObject> {
        return controller.executeCommand(
            AppDatabaseQueryCommand(database, query, packageId),
            deviceId = deviceId
        )
    }
}