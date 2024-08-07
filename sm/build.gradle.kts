plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.jsonSerialization)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    Dependencies.Voyager.getAll().forEach(::implementation)
    Dependencies.HapiFhir.getAll().forEach(::implementation)

    implementation(Dependencies.SqlDelight.coroutineExtension)
    implementation(Dependencies.koin)
    implementation(Dependencies.KotlinX.serializationJson)
    implementation(Dependencies.fileKitCompose)

    implementation(project(":engine"))
    implementation(project(":database"))
    implementation(project(":logcat"))
    implementation(project(":editor"))
    implementation(project(":upload"))
}

