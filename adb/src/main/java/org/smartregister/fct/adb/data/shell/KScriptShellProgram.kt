package org.smartregister.fct.adb.data.shell

import evalBash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.smartregister.fct.adb.domain.program.ShellProgram

class KScriptShellProgram : ShellProgram {

    @Synchronized
    override fun run(command: String): Result<String> {
        //val asyncResult = CoroutineScope(Dispatchers.IO).async {
          return  try {
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
                    Result.success(result)
                }

            } catch (t: Throwable) {
                Result.failure(t)
            }
        /*}
        return asyncResult.await()*/
    }
}