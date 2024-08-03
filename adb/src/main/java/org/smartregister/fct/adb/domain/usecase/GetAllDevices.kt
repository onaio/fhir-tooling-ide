package org.smartregister.fct.adb.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.adb.data.commands.GetAllDevicesCommand
import org.smartregister.fct.adb.data.commands.GetDeviceInfoCommand
import org.smartregister.fct.adb.data.controller.ADBController
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.logcat.FCTLogger

object GetAllDevices : KoinComponent {

    private val controller: ADBController by inject()
    private val devices = MutableSharedFlow<List<Device?>>()
    private var activeDevice: Device? = null

    init {
        start()
    }

    private fun start() {

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            while (true) {
                val deviceList = controller.executeCommand(GetAllDevicesCommand(), shell = false)
                try {
                    if (deviceList.isSuccess) {
                        deviceList.getOrThrow().forEach {
                            val deviceInfo = controller.executeCommand(
                                GetDeviceInfoCommand(),
                                deviceId = it.deviceId
                            )
                            it.deviceInfo = deviceInfo.getOrThrow()
                        }
                    }

                    devices.emit(deviceList.getOrDefault(listOf(null)))
                } catch (ex: Throwable) {
                    FCTLogger.e(ex)
                }
                delay(2000)
            }
        }
    }

    fun setActiveDevice(device: Device?) {
        activeDevice = device
    }

    fun getActiveDevice() : Device? {
        return runBlocking {
            devices
                .firstOrNull()
                ?.let { activeDevice in it }
                ?.let { if(it) activeDevice else null }
        }
    }

    fun getAll(): Flow<List<Device?>> = devices
}