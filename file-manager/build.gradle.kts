plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    Dependencies.Voyager.getAll().forEach(::implementation)

    implementation(Dependencies.SqlDelight.coroutineExtension)
    implementation("ca.gosyer:kotlin-multiplatform-appdirs:1.1.1")
    implementation("com.squareup.okio:okio:3.9.0")

    implementation(project(":engine"))
    implementation(project(":database"))
    implementation(project(":editor"))
    implementation(project(":upload"))
}