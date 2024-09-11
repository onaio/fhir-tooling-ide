package org.smartregister.fct.adb.data.commands

import org.smartregister.fct.adb.data.enums.DeviceType
import org.smartregister.fct.adb.domain.model.DeviceInfo
import org.smartregister.fct.adb.domain.program.ADBCommand
import org.smartregister.fct.adb.utils.CommandConstants
import org.smartregister.fct.adb.utils.resultAsCommandException
import org.smartregister.fct.adb.utils.takeIfNotError
import java.util.Queue

class GetDeviceInfoCommand : ADBCommand<DeviceInfo> {

    override fun process(response: String, dependentResult: Queue<Result<*>>): Result<DeviceInfo> {

        return response
            .takeIfNotError()
            ?.split("\n")
            ?.associate {
                val row = it
                    .replace(": ", ":")
                    .replace("]", "")
                    .replace("[", "")
                    .split(":")

                row[0] to row[1]
            }
            ?.let { map ->

                val deviceType = map[CommandConstants.DEVICE_TYPE]
                    ?.let { if (it.contains("generic")) DeviceType.Virtual else DeviceType.Physical }
                    ?: DeviceType.Unknown

                DeviceInfo(
                    id = map[CommandConstants.DEVICE_ID] ?: "N/A",
                    name = map[CommandConstants.DEVICE_NAME] ?: "N/A",
                    model = map[CommandConstants.DEVICE_MODEL] ?: "N/A",
                    version = map[CommandConstants.DEVICE_OS_VERSION] ?: "N/A",
                    apiLevel = map[CommandConstants.DEVICE_API_LEVEL] ?: "N/A",
                    manufacturer = map[CommandConstants.DEVICE_MANUFACTURER] ?: "N/A",
                    type = deviceType
                )
            }
            ?.let {
                Result.success(it)
            } ?: response.resultAsCommandException()

    }

    override fun build(): List<String> {
        return listOf(
            "getprop",
            "|",
            "grep",
            StringBuilder().apply {
                append("\"")
                append(CommandConstants.DEVICE_ID)
                append("\\|")
                append(CommandConstants.DEVICE_API_LEVEL)
                append("\\|")
                append(CommandConstants.DEVICE_OS_VERSION)
                append("\\|")
                append(CommandConstants.DEVICE_MANUFACTURER)
                append("\\|")
                append(CommandConstants.DEVICE_MODEL)
                append("\\|")
                append(CommandConstants.DEVICE_NAME)
                append("\\|")
                append(CommandConstants.DEVICE_TYPE)
                append("\"")
            }.toString(),
        )
    }
}