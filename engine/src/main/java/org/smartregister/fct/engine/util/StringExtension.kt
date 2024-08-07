package org.smartregister.fct.engine.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.UUID
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun uuid(): String {
    return UUID.randomUUID().toString()
}

fun String.compress(): String {
    val byteStream = ByteArrayOutputStream()
    GZIPOutputStream(byteStream).bufferedWriter().use { it.write(this) }
    return Base64.getEncoder().encodeToString(byteStream.toByteArray())
}

fun String.decompress(): String {
    val compressedBytes = Base64.getDecoder().decode(this)
    val byteArrayInputStream = ByteArrayInputStream(compressedBytes)
    return GZIPInputStream(byteArrayInputStream).bufferedReader().use { it.readText() }
}