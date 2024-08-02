package org.smartregister.fct.adb.domain.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.smartregister.fct.adb.data.commands.GetDeviceInfoCommand
import org.smartregister.fct.adb.data.controller.ADBController

class Device(private val deviceId: String, private val controller: ADBController) {

    fun getDeviceInfo() : Flow<Result<DeviceInfo>> {
        return flow {
            emit(controller.executeCommand(GetDeviceInfoCommand(), deviceId = deviceId))
        }.flowOn(Dispatchers.IO)
    }
}