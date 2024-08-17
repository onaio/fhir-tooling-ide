import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jsonSerialization)
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            Dependencies.Compose.getAll().forEach(::implementation)
            Dependencies.Voyager.getAll().forEach(::implementation)

            implementation(project(":common"))
            implementation(project(":configs"))
            implementation(project(":adb"))
            implementation(project(":device-manager"))
            implementation(project(":package-manager"))
            implementation(project(":structure-map"))
            implementation(project(":file-manager"))
            implementation(project(":logcat"))
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
