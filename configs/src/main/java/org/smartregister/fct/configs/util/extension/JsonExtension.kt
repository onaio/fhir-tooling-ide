package org.smartregister.fct.configs.util.extension

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.smartregister.fct.logcat.FCTLogger

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
    kotlin.runCatching { this.decodeJson<T>(jsonInstance) }
        .onFailure { FCTLogger.e("Failed to decode json") }.getOrNull()

/**
 * Encode the type [T] into a Json string
 *
 * @param jsonInstance the custom json object used to decode the specified string
 * @return the encoded json string from given type [T]
 */
inline fun <reified T> T.encodeJson(jsonInstance: Json? = null): String =
    jsonInstance?.encodeToString(this) ?: json.encodeToString(this)
