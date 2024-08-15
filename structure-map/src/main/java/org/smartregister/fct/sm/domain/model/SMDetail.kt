package org.smartregister.fct.sm.domain.model

import kotlinx.coroutines.coroutineScope
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.logger.FCTLogger

data class SMDetail(
    val id: String,
    val title: String,
    val body: String,
    val source: String? = null,
) {

    suspend fun getSourceResourceName() : String? {
        return try {
            source
                ?.takeIf { it.isNotEmpty()}
                ?.let {
                    coroutineScope {
                        source.decodeResourceFromString<Resource>().resourceType.name
                    }
                }
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            null
        }
    }
}