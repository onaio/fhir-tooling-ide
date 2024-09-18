package org.smartregister.fct.device_database.ui.components

import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.device_database.domain.model.DBInfo

interface QueryDependency {
    suspend fun getRequiredParam(showErrors: Boolean = true, info: suspend (Device, PackageInfo) -> Unit)
}