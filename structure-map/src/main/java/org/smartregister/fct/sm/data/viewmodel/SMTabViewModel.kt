package org.smartregister.fct.sm.data.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.sm.domain.model.SMDetail

class SMTabViewModel(
    var smDetail: SMDetail,
    val codeController: CodeController = CodeController(smDetail.body),
    val inputResource: MutableStateFlow<Resource?> = MutableStateFlow(
        try {
            smDetail.source.decodeResourceFromString<Resource>()
        } catch (ex: Throwable) {
            null
        }
    )
) {
    suspend fun addSource(source: String) : Result<Unit> {
        return try {
           withContext(Dispatchers.IO) {
               if (source.trim().isEmpty()) {
                   inputResource.emit(null)
               } else {
                   val resource = source.decodeResourceFromString<Resource>()
                   smDetail = smDetail.copy(
                       source = source
                   )
                   inputResource.emit(resource)
               }

               Result.success(Unit)
           }
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }
}