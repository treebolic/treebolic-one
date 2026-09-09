/*
 * Copyright (c) 2026. Bernard Bou
 */

plugins {
    alias(libs.plugins.androidLibrary)
}

android {

    namespace = "treebolic.provider.sqlite"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        multiDexEnabled = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.treebolic.model)
    implementation(libs.treebolic.graph)
    implementation(libs.treebolic.loadbalancer)
    implementation(libs.treebolic.provider.sql.generic)

    implementation(project(":treebolicIface"))

    implementation(libs.annotation)

    implementation(libs.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(kotlin("stdlib"))
    coreLibraryDesugaring(libs.desugar)
}
