package org.smartregister.fct.fhirman

import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.smartregister.fct.engine.setup.ModuleSetup
import org.smartregister.fct.logger.FCTLogger

class FhirmanModuleSetup : ModuleSetup {

    private val fhirmanModule = module {

    }

    override suspend fun setup() {
        FCTLogger.d("Loading... Fhirman Module")
        GlobalContext.get().loadModules(listOf(fhirmanModule))
        FCTLogger.d("Fhirman Module Loaded")
    }
}