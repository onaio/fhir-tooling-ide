package org.smartregister.fct.engine.util

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.parser.IParser
import org.hl7.fhir.r4.model.Base
import org.hl7.fhir.r4.model.BaseDateTimeType
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Group
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Practitioner
import org.hl7.fhir.r4.model.PrimitiveType
import org.hl7.fhir.r4.model.Quantity
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.StructureMap
import org.hl7.fhir.r4.model.Timing
import java.util.Locale

const val REFERENCE = "reference"
const val PARTOF = "part-of"

private val fhirR4JsonParser = FhirContext.forR4Cached().newJsonParser()

fun Base?.valueToString(datePattern: String = "dd-MMM-yyyy"): String {
    return when {
        this == null -> return ""
        this.isDateTime -> (this as BaseDateTimeType).value.makeItReadable(datePattern)
        this.isPrimitive -> (this as PrimitiveType<*>).asStringValue()
        this is Coding -> display ?: code
        this is CodeableConcept -> this.stringValue()
        this is Quantity -> this.value.toPlainString()
        this is Timing ->
            this.repeat.let {
                it.period
                    .toPlainString()
                    .plus(" ")
                    .plus(
                        it.periodUnit.display.replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                        },
                    )
                    .plus(" (s)")
            }

        this is HumanName ->
            this.given.firstOrNull().let {
                (if (it != null) "${it.valueToString()} " else "").plus(this.family)
            }

        this is Patient ->
            this.nameFirstRep.nameAsSingleString +
                    ", " +
                    this.gender.name.first() +
                    ", " +
                    this.birthDate.yearsPassed()

        this is Practitioner -> this.nameFirstRep.nameAsSingleString
        this is Group -> this.name
        else -> this.toString()
    }
}

fun CodeableConcept.stringValue(): String =
    this.text ?: this.codingFirstRep.display ?: this.codingFirstRep.code

fun Resource.encodeResourceToString(parser: IParser = fhirR4JsonParser): String =
    parser.encodeResourceToString(this.copy())

fun StructureMap.encodeResourceToString(parser: IParser = fhirR4JsonParser): String =
    parser
        .encodeResourceToString(this)
        .replace("'months'", "\\\\'months\\\\'")
        .replace("'days'", "\\\\'days\\\\'")
        .replace("'years'", "\\\\'years\\\\'")
        .replace("'weeks'", "\\\\'weeks\\\\'")

fun <T> String.decodeResourceFromString(parser: IParser = fhirR4JsonParser): T =
    parser.parseResource(this) as T

fun Resource.asReference() = Reference().apply { this.reference = "$resourceType/$logicalId" }

fun Resource.referenceValue(): String = "$resourceType/$logicalId"

fun String.resourceClassType(): Class<out Resource> =
    FhirContext.forR4Cached().getResourceDefinition(this).implementingClass as Class<out Resource>

/**
 * A function that extracts only the UUID part of a resource logicalId.
 *
 * Examples:
 * 1. "Group/0acda8c9-3fa3-40ae-abcd-7d1fba7098b4/_history/2" returns
 *
 * ```
 *    "0acda8c9-3fa3-40ae-abcd-7d1fba7098b4".
 * ```
 * 2. "Group/0acda8c9-3fa3-40ae-abcd-7d1fba7098b4" returns "0acda8c9-3fa3-40ae-abcd-7d1fba7098b4".
 */
fun String.extractLogicalIdUuid() = this.substringAfter("/").substringBefore("/")


val Resource.logicalId: String
    get() {
        return this.idElement?.idPart.orEmpty()
    }