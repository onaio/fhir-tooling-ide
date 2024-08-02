package org.smartregister.fct.adb.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.smartregister.fct.adb.data.commands.GetAllDevicesCommand
import org.smartregister.fct.adb.data.controller.ADBController
import org.smartregister.fct.adb.domain.model.Device

class GetAllDevices(private val controller: ADBController) {

    operator fun invoke(): Flow<Result<List<Device>>> {
        return flow {
            emit(controller.executeCommand(GetAllDevicesCommand(controller), shell = false))
        }.flowOn(Dispatchers.IO)
    }
}