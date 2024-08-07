package org.smartregister.fct.sm.domain.usecase

import org.smartregister.fct.sm.domain.repository.SMRepository

class DeleteSM(private val smRepository: SMRepository) {
    suspend operator fun invoke(id: String) = smRepository.delete(id)
}