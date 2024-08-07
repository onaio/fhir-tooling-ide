package org.smartregister.fct.sm.data.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.usecase.CreateNewSM
import org.smartregister.fct.sm.domain.usecase.DeleteSM
import org.smartregister.fct.sm.domain.usecase.GetAllSM
import org.smartregister.fct.sm.domain.usecase.UpdateSM

class SMScreenViewModel(private val scope: CoroutineScope) : ScreenModel, KoinComponent {

    private val getAllSM: GetAllSM by inject()
    private val createNewSM: CreateNewSM by inject()
    private val updateSM: UpdateSM by inject()
    private val deleteSM: DeleteSM by inject()

    fun getAllSMList(): Flow<List<SMDetail>> = getAllSM()

    fun insert(smDetail: SMDetail) {
        scope.launch {
            createNewSM(smDetail)
        }
    }

    fun update(smDetail: SMDetail) {
        scope.launch {
            updateSM(smDetail)
        }
    }

    fun delete(id: String) {
        scope.launch {
            deleteSM(id)
        }
    }
}