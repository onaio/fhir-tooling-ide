
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.jsonSerialization)
}

dependencies {
    Dependencies.Koin.getAll().forEach(::api)
    Dependencies.Decompose.getAll().forEach(::api)
    api(Dependencies.KotlinX.serializationJson)
    api(Dependencies.KotlinX.coroutine)
}

