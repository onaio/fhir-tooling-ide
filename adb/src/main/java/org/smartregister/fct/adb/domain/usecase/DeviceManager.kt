package org.smartregister.fct.adb.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.adb.data.commands.GetAllDevicesCommand
import org.smartregister.fct.adb.data.commands.GetDeviceInfoCommand
import org.smartregister.fct.adb.data.controller.ADBController
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.logger.FCTLogger

object DeviceManager : KoinComponent {

    private val controller: ADBController by inject()
    private val devices = MutableSharedFlow<List<Device?>>()
    private val selectedPackage = MutableStateFlow<PackageInfo?>(PackageInfo("org.smartregister.opensrp.gizeir"))
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

                    deviceList.getOrDefault(listOf()).takeIf { it.isEmpty() }?.run {
                        activeDevice = null
                    }
                    devices.emit(deviceList.getOrDefault(listOf(null)))
                } catch (ex: Throwable) {
                    FCTLogger.e(ex)
                }
                delay(4000)
            }
        }
    }

    fun setActiveDevice(device: Device?) {
        activeDevice = device
    }

    fun getActiveDevice() : Device? {
        return activeDevice
    }

    fun getAllDevices(): Flow<List<Device?>> = devices

    fun setActivePackage(packageInfo: PackageInfo?) {
        FCTLogger.i("Package Changed(id = ${packageInfo?.id}, packageId = ${packageInfo?.packageId}, packageName = ${packageInfo?.name})")
        CoroutineScope(Dispatchers.IO).launch {
            selectedPackage.emit(packageInfo)
        }
    }

    fun getActivePackage() : StateFlow<PackageInfo?> = selectedPackage

}