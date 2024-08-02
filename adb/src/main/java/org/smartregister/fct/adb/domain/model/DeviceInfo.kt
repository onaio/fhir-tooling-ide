package org.smartregister.fct.adb.domain.model

import org.smartregister.fct.adb.data.enums.DeviceType

data class DeviceInfo(
    val id: String,
    val name: String,
    val model: String,
    val version: String,
    val apiLevel: String,
    val manufacturer: String,
    val type: DeviceType
)