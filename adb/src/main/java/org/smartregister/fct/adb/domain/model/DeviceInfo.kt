package org.smartregister.fct.adb.domain.model

data class DeviceInfo(
    val name: String,
    val model: String,
    val version: String,
    val apiLevel: String,
    val manufacturer: String
)