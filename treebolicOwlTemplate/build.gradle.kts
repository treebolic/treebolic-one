/*
 * Copyright (c) Treebolic 2019. Bernard Bou <1313ou@gmail.com>
 */

import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties: Properties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {

    namespace = "org.treebolic.owl.template"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.treebolic.owl.template"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get() as String?
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    signingConfigs {
        create("treebolic") {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.txt")
            signingConfig = signingConfigs.getByName("treebolic")
            versionNameSuffix = "signed"
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

configurations.implementation {
    exclude(group = "org.apache.httpcomponents", module = "httpcore-osgi")
    exclude(group = "org.apache.httpcomponents", module = "httpclient-osgi")
}

dependencies {
    implementation(project(":treebolicOneOwl")) // for inclusion)
    implementation(project(":guideLib"))
    implementation(project(":fileChooserLib")) // for manifest)

    implementation(libs.appcompat)

    implementation(libs.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(kotlin("stdlib"))
    coreLibraryDesugaring(libs.desugar)
}
