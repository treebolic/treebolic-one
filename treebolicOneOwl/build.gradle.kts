import java.text.SimpleDateFormat
import java.util.Date
import java.util.Scanner

val buildTime: String = SimpleDateFormat("yyyy-MM-dd_HH:mm").format(Date())

fun getGitHash(): String {
    return try {
        val process = Runtime.getRuntime().exec("git rev-parse --short HEAD")
        val scanner = Scanner(process.inputStream).useDelimiter("\\A")
        if (scanner.hasNext()) scanner.next().trim() else "unknown"
    } catch (e: Exception) {
        "unknown"
    }
}

plugins {
    alias(libs.plugins.androidLibrary)
}

private val vCompileSdk by lazy { rootProject.extra["compileSdk"] as Int }
private val vMinSdk by lazy { rootProject.extra["minSdk"] as Int }

android {

    namespace = "org.treebolic.one.owl"

    compileSdk = vCompileSdk

    defaultConfig {
        minSdk = vMinSdk
        multiDexEnabled = true

        // BuildConfig fields
        buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
        buildConfigField("String", "GIT_HASH", "\"${getGitHash()}\"")
    }

    buildFeatures {
        buildConfig = true
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

configurations.implementation {
    exclude(group = "org.apache.httpcomponents", module = "httpcore-osgi")
    exclude(group = "org.apache.httpcomponents", module = "httpclient-osgi")
}

dependencies {
    implementation(libs.treebolic.model)
    implementation(libs.treebolic.graph)
    implementation(libs.treebolic.mutable)
    implementation(libs.treebolic.view)

    implementation(project(":treebolicIface"))
    implementation(project(":treebolicGlue"))
    implementation(project(":treebolicOwlProvider"))
    implementation(project(":commonLib"))
    implementation(project(":storageLib"))
    implementation(project(":searchLib"))
    implementation(project(":preferenceLib"))
    implementation(project(":downloadLib"))
    implementation(project(":fileChooserLib"))
    implementation(project(":guideLib"))
    implementation(project(":rateLib"))
    implementation(project(":othersLib"))
    implementation(project(":donateLib"))

    implementation(libs.appcompat)
    implementation(libs.preference.ktx)
    implementation(libs.material)

    implementation(libs.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(kotlin("stdlib"))
    coreLibraryDesugaring(libs.desugar)
}
