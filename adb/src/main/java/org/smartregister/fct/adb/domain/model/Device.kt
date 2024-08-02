package org.smartregister.fct.adb.domain.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.adb.data.commands.GetDeviceInfoCommand
import org.smartregister.fct.adb.data.controller.ADBController
import org.smartregister.fct.adb.domain.usecase.GetAllDevices

class Device internal constructor(internal val deviceId: String) : KoinComponent {

    private val controller: ADBController by inject()
    internal lateinit var deviceInfo: DeviceInfo

    fun getDeviceInfo(): DeviceInfo = deviceInfo
}