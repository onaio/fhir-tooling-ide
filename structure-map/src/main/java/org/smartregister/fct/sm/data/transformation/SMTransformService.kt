package org.smartregister.fct.sm.data.transformation

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import org.hl7.fhir.r4.context.SimpleWorkerContext
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.utils.StructureMapUtilities
import org.hl7.fhir.utilities.npm.FilesystemPackageCacheManager
import org.smartregister.fct.sm.data.helper.TransformSupportServices

class SMTransformService {

    private val packageCacheManager: FilesystemPackageCacheManager by lazy {
        FilesystemPackageCacheManager(true)
    }

    private val contextR4: SimpleWorkerContext by lazy {
        SimpleWorkerContext.fromPackage(
            packageCacheManager.loadPackage(
                "hl7.fhir.r4.core",
                "4.0.1"
            )
        )
            .apply { isCanRunWithoutTerminology = true }
    }

    private val transformSupportServices: StructureMapUtilities.ITransformerServices by lazy {
        TransformSupportServices(contextR4)
    }

    private val structureMapUtilities: StructureMapUtilities by lazy {
        StructureMapUtilities(contextR4, transformSupportServices)
    }

    private val jsonParser: IParser by lazy {
        FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
    }

    fun transform(map: String, source: String? = null): Bundle {

        val structureMap = structureMapUtilities.parse(map, "")
        val targetResource = Bundle().apply {
            addEntry().apply {
                resource = structureMap
            }
        }

        if (source != null) {
            val baseElement = jsonParser.parseResource(QuestionnaireResponse::class.java, source)
            structureMapUtilities.transform(contextR4, baseElement, structureMap, targetResource)
        }

        return targetResource
    }
}