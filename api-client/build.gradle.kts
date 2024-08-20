
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.jsonSerialization)
}

dependencies {
    Dependencies.Ktor.getAll().forEach(::implementation)

    implementation(Dependencies.ApacheCommon.compress)
    implementation(Dependencies.gson)
    implementation(Dependencies.KotlinX.serializationJson)

    implementation(project(":common"))
    implementation(project(":logger"))
}

