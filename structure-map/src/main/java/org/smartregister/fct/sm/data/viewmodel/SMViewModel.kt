package org.smartregister.fct.sm.data.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.engine.util.encodeResourceToString
import org.smartregister.fct.engine.util.logicalId
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.sm.data.transformation.SMTransformService
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
    private val transformService: SMTransformService by inject()

    private val activeSMTabViewModel = MutableStateFlow<SMTabViewModel?>(null)
    private val tabViewModels = mutableMapOf<String, SMTabViewModel>()
    private val activeSMResultTabViewModel = MutableStateFlow<SMResultTabViewModel?>(null)
    private val resultTabViewModels = mutableMapOf<String, SMResultTabViewModel>()

    fun init() {
        GlobalScope.launch(Dispatchers.IO) {
            getAllSMList().collectLatest { allSMs ->
                clearActiveSMTabViewModel()
                allSMs.forEachIndexed { index, smDetail ->
                    launch { addSMTabViewModel(smDetail) }

                    if (index == 0) {
                        updateActiveSMTabViewModel(smDetail.id)
                    }
                }
            }
        }
    }

    fun getAllSMList(): Flow<List<SMDetail>> = getAllSM()

    suspend fun insert(smDetail: SMDetail) {
        createNewSM(smDetail)
    }

    suspend fun update(smDetail: SMDetail) {
        updateSM(smDetail)
    }

    suspend fun delete(id: String) {
        tabViewModels.remove(id)
        deleteSM(id)
    }

    fun addSMTabViewModel(smDetail: SMDetail) {
        if (!tabViewModels.containsKey(smDetail.id)) {
            tabViewModels[smDetail.id] = SMTabViewModel(smDetail)
        }
    }

    fun addSMResultTabViewModel(resource: Resource) {
        if (!resultTabViewModels.containsKey(resource.logicalId)) {
            resultTabViewModels[resource.id] = SMResultTabViewModel(resource.encodeResourceToString())
        }
    }

    fun clearAllSMResultTabViewModel() {
        resultTabViewModels.clear()
    }

    suspend fun clearActiveSMTabViewModel() {
        activeSMTabViewModel.emit(null)
    }

    suspend fun updateActiveSMTabViewModel(id: String) {
        activeSMTabViewModel.emit(tabViewModels[id])
    }

    suspend fun updateActiveSMResultTabViewModel(id: String) {
        activeSMResultTabViewModel.emit(resultTabViewModels[id])
    }

    fun getActiveSMTabViewModel(): StateFlow<SMTabViewModel?> = activeSMTabViewModel
    fun getActiveSMResultTabViewModel(): StateFlow<SMResultTabViewModel?> =
        activeSMResultTabViewModel

    suspend fun applyTransformation(
        title: String,
        structureMap: String,
        source: String?
    ): Result<Bundle> {
        return try {
            FCTLogger.i("Start transforming the structure-map $title")

            val result = withContext(Dispatchers.IO) {
                delay(500)
                transformService.transform(
                    structureMap,
                    source
                )
            }

            if (result.isFailure) {
                throw result.exceptionOrNull() ?: RuntimeException()
            }

            Result.success(result.getOrThrow())
        } catch (ex: Throwable) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }
}