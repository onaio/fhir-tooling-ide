package org.smartregister.fct.sm.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.common.util.componentScope
import org.smartregister.fct.common.util.decodeResourceFromString
import org.smartregister.fct.common.util.listOfAllFhirResources
import org.smartregister.fct.common.util.readableResourceName
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.sm.data.transformation.SMTransformService
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.usecase.UpdateSM

class StructureMapTabComponent(
    componentContext: ComponentContext,
    override val smDetail: SMDetail
) :
    TabComponent, KoinComponent,
    ComponentContext by componentContext {

    private val updateStructureMap: UpdateSM by inject()
    private val transformService: SMTransformService by inject()
    override val codeController = instanceKeeper.getOrCreate("code-controller-${smDetail.id}") {
        CodeController(
            scope = componentScope,
            initialText = smDetail.body,
            fileType = FileType.StructureMap
        )
    }

    private val _groups = MutableValue(listOf<String>())
    override val groups: Value<List<String>> = _groups

    private val _outputResources = MutableValue(listOf<String>())
    override val outputResources: Value<List<String>> = _outputResources

    private val savedSourceName = instanceKeeper.getOrCreate("source-name-${smDetail.id}") {
        SourceName()
    }
    private val _sourceName = MutableValue(savedSourceName.name)
    override val sourceName: Value<String> = _sourceName

    init {
        setInitialSourceName()
        listenTextUpdates()
    }

    override fun save() {
        componentScope.launch {
            updateStructureMap(
                smDetail.copy(
                    body = codeController.getText()
                )
            )
        }
    }

    override suspend fun applyTransformation(): Result<Bundle> {
        FCTLogger.i("Start transforming the structure-map ${smDetail.title}")
        delay(500)
        return transformService.transform(
            codeController.getText(),
            smDetail.source
        )
    }

    override suspend fun addSource(source: String): Result<Unit> {
        return try {
            if (source.trim().isEmpty()) {
                smDetail.source = null
                savedSourceName.name = SourceName.DEFAULT_SOURCE_NAME
            } else {
                val resource = source.decodeResourceFromString<Resource>()
                smDetail.source = source
                savedSourceName.name = resource.readableResourceName
            }

            _sourceName.value = savedSourceName.name
            Result.success(Unit)
        } catch (ex: Exception) {
            FCTLogger.e(ex)
            Result.failure(ex)
        }
    }

    private fun setInitialSourceName() {
        componentScope.launch {
            addSource(smDetail.source ?: "")
        }
    }

    private fun listenTextUpdates() {
        componentScope.launch {
            codeController.getTextAsFlow().collectLatest {
                val groupsResult = async { getTotalGroups(it) }
                val outputResourcesResult = async { getOutputResources(it) }
                _groups.value = groupsResult.await()
                _outputResources.value = outputResourcesResult.await()
            }
        }
    }

    private fun getTotalGroups(text: String): List<String> {

        return "(?<=(\\n|\\r|\\s|\\})group\\s)\\w+(?=\\s*\\()"
            .toRegex()
            .findAll(text)
            .map { it.value }
            .toList()
    }

    private fun getOutputResources(text: String): List<String> {
        return "(?<=['\"])\\w+(?=(['\"])\\s*\\)\\s*as)"
            .toRegex()
            .findAll(text)
            .filter { it.value in listOfAllFhirResources }
            .map { it.value }.toList()
    }

    @Serializable
    data class SourceName(var name: String = DEFAULT_SOURCE_NAME) : InstanceKeeper.Instance {

        companion object {
            const val DEFAULT_SOURCE_NAME = "Add Source"
        }
    }
}