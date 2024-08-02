package org.smartregister.fct.adb.data.commands

import org.smartregister.fct.adb.data.controller.ADBController
import org.smartregister.fct.adb.data.exception.CommandException
import org.smartregister.fct.adb.domain.model.ADBCommand
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.utils.CommandConstants
import org.smartregister.fct.adb.utils.asResult
import java.util.Queue

class GetAllDevicesCommand(private val controller: ADBController) : ADBCommand<List<Device>> {

    override fun process(result: Result<String>, dependentResult: Queue<Result<*>>): Result<List<Device>> {
        return result
            .takeIf { it.isSuccess }
            ?.getOrNull()
            ?.takeIf { it.contains("\t") }
            ?.split("\n")
            ?.filter { it.contains("\t") }
            ?.map {
                Device(
                    deviceId = it.split("\t")[0],
                    controller = controller
                )
            }
            ?.toList()
            ?.let { Result.success(it) }
            ?: result.exceptionOrNull().asResult(CommandException("No Device found"))
    }

    override fun build(): List<String> {
        return listOf(CommandConstants.DEVICES)
    }
}