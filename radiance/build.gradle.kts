
plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)

    api("io.github.alexzhirkevich:compottie:2.0.0-beta02")
}