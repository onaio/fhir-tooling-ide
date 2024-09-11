package org.smartregister.fct.device_database.ui.components

import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.device_database.domain.DBInfo

interface QueryDependency {
    fun getRequiredParam(showErrors: Boolean = true, info: (DBInfo, Device, PackageInfo) -> Unit)
}