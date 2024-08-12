import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jsonSerialization)
    id("dev.hydraulic.conveyor") version "1.10"
}

group = "org.smartregister"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    // Use the configurations created by the Conveyor plugin to tell Gradle/Conveyor where to find the artifacts for each platform.
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
// endregion

kotlin {
    jvm("desktop")
    jvmToolchain(17)
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            Dependencies.Compose.getAll().forEach(::implementation)
            Dependencies.Voyager.getAll().forEach(::implementation)

            implementation(Dependencies.koin)

            implementation(project(":engine"))
            implementation(project(":configs"))
            implementation(project(":adb"))
            implementation(project(":device-manager"))
            implementation(project(":package-manager"))
            implementation(project(":structure-map"))
            implementation(project(":file-manager"))
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.smartregister.fct.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.smartregister.fct"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("/icon/app_icon.icns"))
            }

            windows {
                iconFile.set(project.file("/icon/app_icon.ico"))
            }

            linux {
                iconFile.set(project.file("icon/app_icon.png"))
            }
        }
    }
}
