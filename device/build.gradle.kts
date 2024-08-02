
plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    implementation(Dependencies.koin)
    implementation(project(":engine"))
    implementation(project(":adb"))
}