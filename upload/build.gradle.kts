
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)

    implementation(Dependencies.SqlDelight.coroutineExtension)

    api(Dependencies.fileKitCompose)
    implementation(project(":engine"))
    implementation(project(":editor"))
}

