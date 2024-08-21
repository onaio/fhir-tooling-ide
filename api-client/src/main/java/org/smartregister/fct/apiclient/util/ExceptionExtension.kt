package org.smartregister.fct.apiclient.util

import org.hl7.fhir.r4.model.OperationOutcome

internal fun Exception.asOperationOutcome() = OperationOutcome().apply {
    addIssue().apply {
        severity = OperationOutcome.IssueSeverity.ERROR
        code = OperationOutcome.IssueType.EXCEPTION
        diagnostics = message
    }
}