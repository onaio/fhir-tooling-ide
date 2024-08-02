package org.smartregister.fct.device.domain.datasource

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.device.domain.model.Device

interface DeviceManagerDataSource {

    fun getAllConnectedDevice(): Flow<List<Device>>
}