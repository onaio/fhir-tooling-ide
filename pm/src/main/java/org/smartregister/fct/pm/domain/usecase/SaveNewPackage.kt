package org.smartregister.fct.pm.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.pm.domain.repository.PackageRepository

class SaveNewPackage(private val repository: PackageRepository) {

    operator fun invoke(id: String, packageId: String, packageName: String) = repository.insert(
        id = id,
        packageId = packageId,
        packageName = packageName
    )
}