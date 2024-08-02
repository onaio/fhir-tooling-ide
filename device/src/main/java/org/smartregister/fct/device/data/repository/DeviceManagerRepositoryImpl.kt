package org.smartregister.fct.device.data.repository

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.device.domain.datasource.DeviceManagerDataSource
import org.smartregister.fct.device.domain.model.Device

class DeviceManagerRepositoryImpl(private val deviceManagerDataSource: DeviceManagerDataSource) {

    fun getAllConnectedDevice(): Flow<List<Device>> = deviceManagerDataSource.getAllConnectedDevice()
}