package org.smartregister.fct.device.domain.repository

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.device.domain.model.Device

interface DeviceManagerRepository {
    fun getAllConnectedDevice(): Flow<List<Device>>
}