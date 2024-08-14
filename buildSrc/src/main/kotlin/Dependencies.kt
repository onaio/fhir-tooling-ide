/*
 * Copyright 2023-2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Dependencies {

    object Version {
        const val kotlinXCoroutine = "1.9.0-RC"
        const val kotlinXSerializationJson = "1.7.1"
        const val compose = "1.6.11"
        const val voyager = "1.1.0-beta02"
        const val fhirVersion = "6.8.0"
        const val hapiFhirCore = "6.0.22"
        const val koin = "4.0.0-RC1"
        const val sqlDelight = "2.0.2"
        const val fileKitCompose = "0.7.0"
        const val kScriptTool = "1.0.22"
        const val constraintLayoutCompose = "0.4.0"
        const val json = "20240303"
        const val prettyTime = "5.0.9.Final"
        const val apacheCommonCollection = "4.5.0-M2"
        const val apacheCommonCompress = "1.26.2"
        const val apacheCommonIO = "2.16.1"
        const val gson = "2.11.0"
        const val compottie = "2.0.0-beta02"
        const val okio = "3.9.0"
    }

    const val fileKitCompose = "io.github.vinceglb:filekit-compose:${Version.fileKitCompose}"
    const val kScriptTool = "com.sealwu:kscript-tools:${Version.kScriptTool}"

    const val json = "org.json:json:${Version.json}"
    const val prettyTime = "org.ocpsoft.prettytime:prettytime:${Version.prettyTime}"
    const val gson = "com.google.code.gson:gson:${Version.gson}"
    const val compottie = "io.github.alexzhirkevich:compottie:${Version.compottie}"


    object Koin {
        const val core = "io.insert-koin:koin-core:${Version.koin}"
        const val compose = "io.insert-koin:koin-compose:${Version.koin}"

        fun getAll(): List<String> = listOf(core, compose)
    }

    object KotlinX {
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinXCoroutine}"
        const val serializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinXSerializationJson}"

        fun getAll(): List<String> = listOf(coroutine, serializationJson)
    }

    object Compose {

        const val runtime = "org.jetbrains.compose.runtime:runtime:${Version.compose}"
        const val foundation = "org.jetbrains.compose.foundation:foundation:${Version.compose}"
        const val material3Desktop =
            "org.jetbrains.compose.material3:material3-desktop:${Version.compose}"
        const val ui = "org.jetbrains.compose.ui:ui:${Version.compose}"
        const val componentResources =
            "org.jetbrains.compose.components:components-resources:${Version.compose}"
        const val componentsUiTollingPreview =
            "org.jetbrains.compose.components:components-ui-tooling-preview:${Version.compose}"
        const val materialIconsExtended =
            "org.jetbrains.compose.material:material-icons-extended:${Version.compose}"

        const val constraintLayoutCompose = "tech.annexflow.compose:constraintlayout-compose-multiplatform:${Version.constraintLayoutCompose}"

        fun getAll(): List<String> = listOf(
            runtime,
            foundation,
            material3Desktop,
            ui,
            componentResources,
            componentsUiTollingPreview,
            materialIconsExtended,
            constraintLayoutCompose
        )
    }

    object Voyager {
        const val voyagerVersion = "1.1.0-beta02"

        const val navigator = "cafe.adriel.voyager:voyager-navigator:${Version.voyager}"
        const val screenModel = "cafe.adriel.voyager:voyager-screenmodel:${Version.voyager}"
        const val bottomSheetNavigator =
            "cafe.adriel.voyager:voyager-bottom-sheet-navigator:${Version.voyager}"
        const val tabNavigator = "cafe.adriel.voyager:voyager-tab-navigator:${Version.voyager}"
        const val transition = "cafe.adriel.voyager:voyager-transitions:${Version.voyager}"
        const val lifecycleKmp = "cafe.adriel.voyager:voyager-lifecycle-kmp:${Version.voyager}"
        const val koin = "cafe.adriel.voyager:voyager-koin:${Version.voyager}"

        fun getAll() = listOf(
            navigator,
            screenModel,
            bottomSheetNavigator,
            tabNavigator,
            transition,
            lifecycleKmp,
            koin
        )
    }

    object HapiFhir {

        const val base = "ca.uhn.hapi.fhir:hapi-fhir-base:${Version.fhirVersion}"
        const val structuresR4 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4:${Version.fhirVersion}"
        const val client = "ca.uhn.hapi.fhir:hapi-fhir-client:${Version.fhirVersion}"
        const val structuresDstu2 =
            "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2:${Version.fhirVersion}"
        const val fhirR4 = "ca.uhn.hapi.fhir:org.hl7.fhir.r4:${Version.hapiFhirCore}"
        const val fhirR4B = "ca.uhn.hapi.fhir:org.hl7.fhir.r4b:${Version.hapiFhirCore}"
        const val validation = "ca.uhn.hapi.fhir:hapi-fhir-validation:${Version.fhirVersion}"
        const val fhirCoreUtilsModule = "ca.uhn.hapi.fhir:org.hl7.fhir.utilities:${Version.hapiFhirCore}"
        const val guavaCachingModule = "ca.uhn.hapi.fhir:hapi-fhir-caching-guava:${Version.fhirVersion}"

        fun getAll() = listOf(base, structuresR4, client, structuresDstu2, fhirR4, fhirR4B, validation)
    }

    object SqlDelight {
        const val sqliteDriver = "app.cash.sqldelight:sqlite-driver:${Version.sqlDelight}"
        const val coroutineExtension = "app.cash.sqldelight:coroutines-extensions:${Version.sqlDelight}"

        fun getAll() = listOf(sqliteDriver, coroutineExtension)
    }

    object ApacheCommon {

        const val collection = "org.apache.commons:commons-collections4:${Version.apacheCommonCollection}"
        const val compress = "org.apache.commons:commons-compress:${Version.apacheCommonCompress}"
        const val io = "commons-io:commons-io:${Version.apacheCommonIO}"

        fun getAll() = listOf(collection, compress, io)
    }

    object Squareup {
        const val okio = "com.squareup.okio:okio:${Version.okio}"
    }
}

/*
object Dependencies {

  object HapiFhir {
    const val fhirBaseModule = "ca.uhn.hapi.fhir:hapi-fhir-base"
    const val fhirClientModule = "ca.uhn.hapi.fhir:hapi-fhir-client"
    const val structuresDstu2Module = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2"
    const val structuresDstu3Module = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu3"
    const val structuresR4Module = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4"
    const val structuresR4bModule = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4b"
    const val structuresR5Module = "ca.uhn.hapi.fhir:hapi-fhir-structures-r5"

    const val validationModule = "ca.uhn.hapi.fhir:hapi-fhir-validation"
    const val validationDstu3Module = "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-dstu3"
    const val validationR4Module = "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4"
    const val validationR5Module = "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r5"

    const val fhirCoreDstu2Module = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2"
    const val fhirCoreDstu2016Module = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2016may"
    const val fhirCoreDstu3Module = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu3"
    const val fhirCoreR4Module = "ca.uhn.hapi.fhir:org.hl7.fhir.r4"
    const val fhirCoreR4bModule = "ca.uhn.hapi.fhir:org.hl7.fhir.r4b"
    const val fhirCoreR5Module = "ca.uhn.hapi.fhir:org.hl7.fhir.r5"
    const val fhirCoreUtilsModule = "ca.uhn.hapi.fhir:org.hl7.fhir.utilities"
    const val fhirCoreConvertorsModule = "ca.uhn.hapi.fhir:org.hl7.fhir.convertors"

    const val guavaCachingModule = "ca.uhn.hapi.fhir:hapi-fhir-caching-guava"

    const val fhirBase = "$fhirBaseModule:${Versions.hapiFhir}"
    const val fhirClient = "$fhirClientModule:${Versions.hapiFhir}"
    const val structuresDstu2 = "$structuresDstu2Module:${Versions.hapiFhir}"
    const val structuresDstu3 = "$structuresDstu3Module:${Versions.hapiFhir}"
    const val structuresR4 = "$structuresR4Module:${Versions.hapiFhir}"
    const val structuresR4b = "$structuresR4bModule:${Versions.hapiFhir}"
    const val structuresR5 = "$structuresR5Module:${Versions.hapiFhir}"

    const val validation = "$validationModule:${Versions.hapiFhir}"
    const val validationDstu3 = "$validationDstu3Module:${Versions.hapiFhir}"
    const val validationR4 = "$validationR4Module:${Versions.hapiFhir}"
    const val validationR5 = "$validationR5Module:${Versions.hapiFhir}"

    const val fhirCoreDstu2 = "$fhirCoreDstu2Module:${Versions.hapiFhirCore}"
    const val fhirCoreDstu2016 = "$fhirCoreDstu2016Module:${Versions.hapiFhirCore}"
    const val fhirCoreDstu3 = "$fhirCoreDstu3Module:${Versions.hapiFhirCore}"
    const val fhirCoreR4 = "$fhirCoreR4Module:${Versions.hapiFhirCore}"
    const val fhirCoreR4b = "$fhirCoreR4bModule:${Versions.hapiFhirCore}"
    const val fhirCoreR5 = "$fhirCoreR5Module:${Versions.hapiFhirCore}"
    const val fhirCoreUtils = "$fhirCoreUtilsModule:${Versions.hapiFhirCore}"
    const val fhirCoreConvertors = "$fhirCoreConvertorsModule:${Versions.hapiFhirCore}"

    const val guavaCaching = "$guavaCachingModule:${Versions.hapiFhir}"
  }

  object Jackson {
    private const val mainGroup = "com.fasterxml.jackson"
    private const val coreGroup = "$mainGroup.core"
    private const val dataformatGroup = "$mainGroup.dataformat"
    private const val datatypeGroup = "$mainGroup.datatype"
    private const val moduleGroup = "$mainGroup.module"

    const val annotationsBase = "$coreGroup:jackson-annotations:${Versions.jackson}"
    const val bomBase = "$mainGroup:jackson-bom:${Versions.jackson}"
    const val coreBase = "$coreGroup:jackson-core:${Versions.jacksonCore}"
    const val databindBase = "$coreGroup:jackson-databind:${Versions.jackson}"
    const val dataformatXmlBase = "$dataformatGroup:jackson-dataformat-xml:${Versions.jackson}"
    const val jaxbAnnotationsBase =
      "$moduleGroup:jackson-module-jaxb-annotations:${Versions.jackson}"
    const val jsr310Base = "$datatypeGroup:jackson-datatype-jsr310:${Versions.jackson}"
  }

  object Versions {
    const val hapiFhir = "6.8.0"
    const val hapiFhirCore = "6.0.22"

    // Maximum Jackson libraries (excluding core) version that supports Android API Level 24:
    // https://github.com/FasterXML/jackson-databind/issues/3658
    const val jackson = "2.13.5"

    // Maximum Jackson Core library version that supports Android API Level 24:
    const val jacksonCore = "2.15.2"
  }

  fun Configuration.removeIncompatibleDependencies() {
    exclude(module = "xpp3")
    exclude(module = "xpp3_min")
    exclude(module = "xmlpull")
    exclude(module = "javax.json")
    exclude(module = "jcl-over-slf4j")
    exclude(group = "org.apache.httpcomponents")
    exclude(group = "org.antlr", module = "antlr4")
    exclude(group = "org.eclipse.persistence", module = "org.eclipse.persistence.moxy")
    exclude(module = "hapi-fhir-caching-caffeine")
    exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")
  }

  fun hapiFhirConstraints(): Map<String, DependencyConstraint.() -> Unit> {
    return mutableMapOf<String, DependencyConstraint.() -> Unit>(
      HapiFhir.fhirBaseModule to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.fhirClientModule to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.fhirCoreConvertorsModule to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreDstu2Module to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreDstu2016Module to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreDstu3Module to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreR4Module to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreR4bModule to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreR5Module to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.fhirCoreUtilsModule to { version { strictly(Versions.hapiFhirCore) } },
      HapiFhir.structuresDstu2Module to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.structuresDstu3Module to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.structuresR4Module to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.structuresR5Module to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.validationModule to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.validationDstu3Module to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.validationR4Module to { version { strictly(Versions.hapiFhir) } },
      HapiFhir.validationR5Module to { version { strictly(Versions.hapiFhir) } },
              Jackson.annotationsBase to { version { strictly(Versions.jackson) } },
      Jackson.bomBase to { version { strictly(Versions.jackson) } },
      Jackson.coreBase to { version { strictly(Versions.jacksonCore) } },
      Jackson.databindBase to { version { strictly(Versions.jackson) } },
      Jackson.jaxbAnnotationsBase to { version { strictly(Versions.jackson) } },
      Jackson.jsr310Base to { version { strictly(Versions.jackson) } },
      Jackson.dataformatXmlBase to { version { strictly(Versions.jackson) } },
    )
  }
}
*/