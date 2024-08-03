package org.smartregister.fct.pm.domain.usecase

import org.smartregister.fct.pm.domain.repository.PackageRepository

class DeletePackage(private val repository: PackageRepository) {

    operator fun invoke(id: String) = repository.delete(id = id)
}