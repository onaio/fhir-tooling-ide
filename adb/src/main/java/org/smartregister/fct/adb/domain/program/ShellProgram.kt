package org.smartregister.fct.adb.domain.program

interface ShellProgram {

    suspend fun run(command: String): Result<String>
}