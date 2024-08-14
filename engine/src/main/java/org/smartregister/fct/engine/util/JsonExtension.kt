package org.smartregister.fct.engine.util

import com.google.gson.FormattingStyle
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject
import org.smartregister.fct.engine.ui.viewmodel.AppSettingViewModel
import org.smartregister.fct.logger.FCTLogger

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    isLenient = true
    useAlternativeNames = true
    prettyPrint = true
    explicitNulls = false
}

/**
 * Decode string to an entity of type [T]
 *
 * @param jsonInstance the custom json object used to decode the specified string
 * @return the decoded object of type [T]
 */
inline fun <reified T> String.decodeJson(jsonInstance: Json? = null): T =
    jsonInstance?.decodeFromString(this) ?: json.decodeFromString(this)

/**
 * Decode string to an entity of type [T] or return null if json is invalid
 *
 * @param jsonInstance the custom json object used to decode the specified string
 * @return the decoded object of type [T]
 */
inline fun <reified T> String.tryDecodeJson(jsonInstance: Json? = null): T? =
    kotlin.runCatching { this.decodeJson<T>(jsonInstance) }.getOrNull()

/**
 * Encode the type [T] into a Json string
 *
 * @param jsonInstance the custom json object used to decode the specified string
 * @return the encoded json string from given type [T]
 */
inline fun <reified T> T.encodeJson(jsonInstance: Json? = null): String =
    jsonInstance?.encodeToString(this) ?: json.encodeToString(this)

suspend fun String.prettyJson() : String {

    val appSetting = getKoinInstance<AppSettingViewModel>().appSetting

    return withContext(Dispatchers.IO) {
        try {
            val gson = GsonBuilder()
                .disableHtmlEscaping()
                .setFormattingStyle(FormattingStyle.PRETTY.withIndent(" ".repeat(appSetting.codeEditorConfig.indent)))
                .create()
            val jsonParser = JsonParser.parseString(this@prettyJson).asJsonObject
            gson.toJson(jsonParser)
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            this@prettyJson
        }
    }
}
