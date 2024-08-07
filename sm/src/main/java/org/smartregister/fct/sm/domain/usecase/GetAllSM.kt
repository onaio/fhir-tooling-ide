package org.smartregister.fct.sm.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.repository.SMRepository

class GetAllSM(private val smRepository: SMRepository) {
    operator fun invoke(): Flow<List<SMDetail>> = smRepository.getAll()
}