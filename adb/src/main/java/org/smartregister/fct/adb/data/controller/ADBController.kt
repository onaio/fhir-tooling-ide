package org.smartregister.fct.adb.data.controller

import org.smartregister.fct.adb.domain.program.ADBCommand
import org.smartregister.fct.adb.domain.program.ShellProgram
import org.smartregister.fct.logger.FCTLogger
import java.util.LinkedList
import java.util.Queue

@Suppress("UNCHECKED_CAST")
class ADBController(private val shellProgram: ShellProgram) {

    suspend fun <T> executeCommand(
        command: ADBCommand<T>,
        deviceId: String? = null,
        shell: Boolean = true,
        strictDependent: Boolean = true
    ): Result<T> {

        val dependentResult: Queue<Result<*>> = LinkedList()
        if (command.getDependentCommands().isNotEmpty()) {
            executeBatch(command.getDependentCommands().remove(), deviceId, shell, dependentResult)
        }

        return execute(command, deviceId, shell, dependentResult) as Result<T>
    }

    private suspend fun executeBatch(
        command: ADBCommand<*>,
        deviceId: String? = null,
        shell: Boolean = true,
        dependentResult: Queue<Result<*>>
    ) {
        if (command.getDependentCommands().isNotEmpty()) {
            executeBatch(command.getDependentCommands().remove(), deviceId, shell, dependentResult)
        } else {
            dependentResult.add(execute(command, deviceId, shell, dependentResult))
        }
    }

    private suspend fun execute(
        command: ADBCommand<*>,
        deviceId: String? = null,
        shell: Boolean = true,
        dependentResult: Queue<Result<*>>
    ): Result<*> {

        val commandList = mutableListOf<String>().apply {
            add("adb")
            if (deviceId != null) {
                add("-s")
                add(deviceId)
            }
            if (shell) add("shell")
            addAll(command.build())
        }

        val result = shellProgram.run(commandList.joinToString(" ")
            .also { FCTLogger.d(it, tag = command.javaClass.simpleName) }
        )

       return if (result.isSuccess) {
           try {
               command.process(result.getOrThrow(), dependentResult)
           } catch (t: Throwable) {
               FCTLogger.e(t)
               result
           }
        } else {
            result
        }
    }
}