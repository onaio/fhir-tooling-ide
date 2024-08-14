
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    implementation(Dependencies.SqlDelight.coroutineExtension)

    implementation(project(":engine"))
    implementation(project(":database"))
    implementation(project(":adb"))
}

