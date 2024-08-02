package org.smartregister.fct.adb.data.shell

import evalBash
import org.smartregister.fct.adb.domain.program.ShellProgram

class KScriptShellProgram : ShellProgram {

    override suspend fun run(command: String): Result<String> {
        return try {
            Result.success(evalBash(command).getOrThrow())
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }
}