
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    Dependencies.HapiFhir.getAll().forEach(::implementation)

    //implementation(Dependencies.composeView)
    implementation(Dependencies.json)

    implementation(project(":common"))
    implementation(project(":api-client"))
    implementation(project(":settings"))
    implementation(project(":adb"))
}