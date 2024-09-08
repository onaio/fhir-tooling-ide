package org.smartregister.fct.sm.data.transformation

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hl7.fhir.r4.context.SimpleWorkerContext
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Parameters
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.utils.StructureMapUtilities
import org.hl7.fhir.utilities.npm.FilesystemPackageCacheManager
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.sm.data.helper.TransformSupportServices

class SMTransformService {

    private lateinit var packageCacheManager: FilesystemPackageCacheManager
    private lateinit var contextR4: SimpleWorkerContext
    private lateinit var transformSupportServices: StructureMapUtilities.ITransformerServices
    private lateinit var structureMapUtilities: StructureMapUtilities
    private lateinit var jsonParser: IParser

    suspend fun init() = withContext(Dispatchers.IO) {
        packageCacheManager = FilesystemPackageCacheManager(true)

        contextR4 = SimpleWorkerContext.fromPackage(
            packageCacheManager.loadPackage(
                "hl7.fhir.r4.core",
                "4.0.1"
            )
        ).apply {
            setExpansionProfile(Parameters())
            isCanRunWithoutTerminology = true
        }

        transformSupportServices = TransformSupportServices(contextR4)

        structureMapUtilities = StructureMapUtilities(contextR4, transformSupportServices)

        jsonParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
    }

    fun transform(map: String, source: String? = null): Result<Bundle> {
        return try {
            val structureMap = structureMapUtilities.parse(map, "")

            val targetResource = Bundle().apply {
                addEntry().apply {
                    resource = structureMap
                }
            }

            if (source != null) {
                val clazz = source.decodeResourceFromString<Resource>().javaClass
                val baseElement = jsonParser.parseResource(clazz, source)
                structureMapUtilities.transform(
                    contextR4,
                    baseElement,
                    structureMap,
                    targetResource
                )
            }

            Result.success(targetResource)
        } catch (ex: Throwable) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }
}