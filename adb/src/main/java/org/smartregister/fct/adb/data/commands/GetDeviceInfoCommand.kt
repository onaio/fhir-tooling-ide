package org.smartregister.fct.adb.data.commands

import org.smartregister.fct.adb.data.exception.CommandException
import org.smartregister.fct.adb.domain.model.ADBCommand
import org.smartregister.fct.adb.domain.model.DeviceInfo
import org.smartregister.fct.adb.utils.CommandConstants
import org.smartregister.fct.adb.utils.asResult
import java.util.Queue

class GetDeviceInfoCommand : ADBCommand<DeviceInfo> {

    override fun process(result: Result<String>, dependentResult: Queue<Result<*>>): Result<DeviceInfo> {

        return result
            .takeIf { result.isSuccess }
            ?.getOrNull()
            ?.takeUnless { it.contains("error: ") }
            ?.split("\n")
            ?.associate {
                val row = it
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .split(":")

                row[0] to row[1]
            }
            ?.let {
                DeviceInfo(
                    name = it[CommandConstants.DEVICE_NAME] ?: "N/A",
                    model = it[CommandConstants.DEVICE_MODEL] ?: "N/A",
                    version = it[CommandConstants.DEVICE_OS_VERSION] ?: "N/A",
                    apiLevel = it[CommandConstants.DEVICE_API_LEVEL] ?: "N/A",
                    manufacturer = it[CommandConstants.DEVICE_MANUFACTURER] ?: "N/A"
                )
            }
            ?.let {
                Result.success(it)
            } ?: result
                .exceptionOrNull()
                .asResult()

    }

    override fun build(): List<String> {
        return listOf(
            "getprop",
            "|",
            "grep",
            StringBuilder().apply {
                append("\"")
                append(CommandConstants.DEVICE_API_LEVEL)
                append("\\|")
                append(CommandConstants.DEVICE_OS_VERSION)
                append("\\|")
                append(CommandConstants.DEVICE_MANUFACTURER)
                append("\\|")
                append(CommandConstants.DEVICE_MODEL)
                append("\\|")
                append(CommandConstants.DEVICE_NAME)
                append("\"")
            }.toString(),
        )
    }
}