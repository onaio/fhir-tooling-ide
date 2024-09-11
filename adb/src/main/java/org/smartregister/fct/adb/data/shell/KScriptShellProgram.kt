package org.smartregister.fct.adb.data.shell

import evalBash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.smartregister.fct.adb.domain.program.ShellProgram

class KScriptShellProgram : ShellProgram {

    override suspend fun run(command: String): Result<String> {
        val asyncResult = CoroutineScope(Dispatchers.IO).async {
            try {
                val evalBash = evalBash(command)
                val result = evalBash.getOrThrow()

                if (result.trim().isEmpty() && evalBash.stderr.toList().isNotEmpty()) {
                    val errorMessage = evalBash
                        .stderr
                        .filter { !it.contains("\t") }
                        .joinToString("\n")
                        .trim()
                    Result.failure(RuntimeException(errorMessage))
                } else {
                    Result.success(evalBash(command).getOrThrow())
                }

            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
        return asyncResult.await()
    }
}