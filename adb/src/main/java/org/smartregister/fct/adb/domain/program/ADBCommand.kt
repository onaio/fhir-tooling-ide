package org.smartregister.fct.adb.domain.program

import java.util.LinkedList
import java.util.Queue

interface ADBCommand<T> {

    fun process(result: String, dependentResult: Queue<Result<*>>): Result<T>
    fun build(): List<String>
    fun getDependentCommands(): Queue<ADBCommand<*>> = LinkedList()
}