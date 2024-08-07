package org.smartregister.fct.sm.data.viewmodel

import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.usecase.CreateNewSM
import org.smartregister.fct.sm.domain.usecase.DeleteSM
import org.smartregister.fct.sm.domain.usecase.GetAllSM
import org.smartregister.fct.sm.domain.usecase.UpdateSM

class SMViewModel : KoinComponent {

    private val getAllSM: GetAllSM by inject()
    private val createNewSM: CreateNewSM by inject()
    private val updateSM: UpdateSM by inject()
    private val deleteSM: DeleteSM by inject()

    fun getAllSMList(): Flow<List<SMDetail>> = getAllSM()

    suspend fun insert(smDetail: SMDetail) {
        createNewSM(smDetail)
    }

    suspend fun update(smDetail: SMDetail) {
        updateSM(smDetail)
    }

    suspend fun delete(id: String) {
        deleteSM(id)
    }
}