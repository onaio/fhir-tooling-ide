package org.smartregister.fct.sm.domain.usecase

import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.repository.SMRepository

class CreateNewSM(private val smRepository: SMRepository) {
    suspend operator fun invoke(smDetail: SMDetail) = smRepository.insert(smDetail)
}