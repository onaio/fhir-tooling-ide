package org.smartregister.fct.adb.domain.program

import org.smartregister.fct.adb.domain.model.CommandResult
import java.util.Queue

interface ADBCommand<T> {

    fun process(response: String, dependentResult: List<CommandResult<*>>): Result<T>
    fun build(): List<String>
    fun getDependentCommands(): List<ADBCommand<*>> = ArrayList()
}