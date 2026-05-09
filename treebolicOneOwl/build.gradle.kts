import java.text.SimpleDateFormat
import java.util.Date

val buildTime: String = SimpleDateFormat("yyyy-MM-dd_HH:mm").format(Date())

fun getGitHash(workingDir: File): String? {
    return try {
        val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
            .directory(workingDir)
            .redirectErrorStream(true)
            .start()
        val result = process.inputStream.bufferedReader().use { it.readText() }.trim()
        val exitCode = process.waitFor()
        if (exitCode == 0) result else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

plugins {
    alias(libs.plugins.androidLibrary)
}

android {

    namespace = "org.treebolic.one.owl"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        multiDexEnabled = true

        // BuildConfig fields
        buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
        buildConfigField("String", "GIT_HASH", "\"${getGitHash(File("TreebolicOne"))}\"")
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
