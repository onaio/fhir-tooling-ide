
plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.sqlDelight)
}

dependencies {
    Dependencies.SqlDelight.getAll().forEach(::implementation)

    implementation(Dependencies.koin)
    implementation(Dependencies.KotlinX.serializationJson)
    implementation(Dependencies.SqlDelight.sqliteDriver)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("org.smartregister.fct.database")
            srcDirs.setFrom("src/main/sqldelight")
        }
    }
}