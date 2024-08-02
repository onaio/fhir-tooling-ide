package org.smartregister.fct.device.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.device.domain.model.Device
import org.smartregister.fct.device.domain.repository.DeviceManagerRepository

class GetAllConnectedDevice(private val repository: DeviceManagerRepository) {

    operator fun invoke() : Flow<List<Device>> = repository.getAllConnectedDevice()
}