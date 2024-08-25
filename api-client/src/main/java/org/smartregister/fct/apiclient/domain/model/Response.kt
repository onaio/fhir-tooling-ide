package org.smartregister.fct.apiclient.domain.model

import org.hl7.fhir.r4.model.OperationOutcome

sealed class Response {

    data class Success(val response: String) : Response()
    data class Failed(
        val outcome: OperationOutcome
    ) : Response()
}

