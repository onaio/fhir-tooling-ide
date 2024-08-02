
plugins {
    //id("java-library")
    //kotlin("jvm") version "2.0.0"
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(Dependencies.kScriptTool)
    implementation(Dependencies.KotlinX.coroutine)
    implementation(project(":logcat"))
}