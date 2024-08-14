plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

dependencies {
    Dependencies.Compose.getAll().forEach(::implementation)
    Dependencies.Voyager.getAll().forEach(::implementation)

    implementation(Dependencies.SqlDelight.coroutineExtension)
    implementation(Dependencies.Squareup.okio)
    implementation(Dependencies.ApacheCommon.io)

    implementation("io.github.ltttttttttttt:ComposeViews:1.6.11.4")

    implementation(project(":engine"))
    implementation(project(":database"))
    implementation(project(":editor"))
    implementation(project(":upload"))
}