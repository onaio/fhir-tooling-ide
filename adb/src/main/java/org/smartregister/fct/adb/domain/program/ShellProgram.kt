package org.smartregister.fct.adb.domain.program

interface ShellProgram {

    fun run(command: String): Result<String>
}