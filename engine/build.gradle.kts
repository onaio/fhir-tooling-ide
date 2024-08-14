
plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jsonSerialization)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    Dependencies.HapiFhir.getAll().forEach(::implementation)
    Dependencies.Koin.getAll().forEach(::api)

    implementation(Dependencies.gson)
    implementation(Dependencies.KotlinX.serializationJson)
    implementation(Dependencies.SqlDelight.coroutineExtension)
    implementation(Dependencies.prettyTime)
    implementation(project(":database"))


    api(project(":logger"))
    api(project(":aurora"))
    api(project(":json-tree"))
}