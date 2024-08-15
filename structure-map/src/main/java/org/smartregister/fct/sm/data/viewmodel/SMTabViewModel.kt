package org.smartregister.fct.sm.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.sm.domain.model.SMDetail

class SMTabViewModel(
    var smDetail: SMDetail,
) {

    private val smDetailState: MutableStateFlow<SMDetail> = MutableStateFlow(smDetail)
    val codeController: CodeController;

    init {

        val scope = CoroutineScope(Dispatchers.Default)
        codeController = CodeController(scope, smDetail.body, FileType.StructureMap)
    }

    fun getSMDetailState(): StateFlow<SMDetail> = smDetailState

    suspend fun addSource(source: String) : Result<Unit> {
        return try {
           withContext(Dispatchers.IO) {
               smDetail = if (source.trim().isEmpty()) {
                   smDetail.copy(
                       source = null
                   )
               } else {
                   source.decodeResourceFromString<Resource>()
                   smDetail.copy(
                       source = source
                   )
               }

               smDetailState.emit(smDetail)
               Result.success(Unit)
           }
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }
}