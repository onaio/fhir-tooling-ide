package org.smartregister.fct.device.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.smartregister.fct.device.domain.model.Device
import org.smartregister.fct.device.domain.datasource.DeviceManagerDataSource

class ADBDeviceManagerDataSource : DeviceManagerDataSource {

    override fun getAllConnectedDevice(): Flow<List<Device>> {
        return flowOf()
    }
}