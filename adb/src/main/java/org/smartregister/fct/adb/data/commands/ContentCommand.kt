package org.smartregister.fct.adb.data.commands

import org.json.JSONObject
import org.smartregister.fct.adb.domain.program.ADBCommand
import org.smartregister.fct.engine.util.compress
import org.smartregister.fct.engine.util.decompress
import org.smartregister.fct.engine.util.replaceLast
import org.smartregister.fct.logger.FCTLogger
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.Queue
import java.util.zip.GZIPOutputStream

abstract class ContentCommand : ADBCommand<JSONObject> {

    override fun process(response: String, dependentResult: Queue<Result<*>>): Result<JSONObject> {
        return try {
            val sanitizeResponse = response.replace("Result: Bundle[{data=", "").replaceLast("}]", "")
            val jsonObject = JSONObject(sanitizeResponse.decompress())
            if (jsonObject.getBoolean("success")) {
                Result.success(jsonObject)
            } else {
                Result.failure(RuntimeException(jsonObject.getString("error")))
            }
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }

    override fun build(): List<String> {
        return listOf(
            "content",
            "call",
            "--uri",
            "'content://${getPackageName()}'",
            "--method",
            "'${getMethodName()}'",
            "--arg",
            "'${getCompressedArgument()}'"
        )
    }

    private fun getCompressedArgument(): String {
        val arg = getArgument()
        FCTLogger.d("Content Request: $arg")
        if (arg.trim().isEmpty()) return arg
        return arg.compress()
    }

    abstract fun getPackageName(): String

    abstract fun getMethodName(): String


    open fun getArgument(): String = ""
}