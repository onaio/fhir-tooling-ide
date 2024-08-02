package org.smartregister.fct.adb.utils

internal object CommandConstants {
    const val DEVICES = "devices"
    const val DEVICE_API_LEVEL = "ro.build.version.sdk"
    const val DEVICE_OS_VERSION = "ro.build.version.release"
    const val DEVICE_MANUFACTURER = "ro.product.vendor.manufacturer"
    const val DEVICE_MODEL = "ro.product.vendor.model"
    const val DEVICE_NAME = "ro.product.vendor.name"
}