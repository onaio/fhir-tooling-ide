
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(Dependencies.json)
    implementation(Dependencies.KotlinX.coroutine)
    implementation(project(":common"))
    implementation(project(":shell"))
}