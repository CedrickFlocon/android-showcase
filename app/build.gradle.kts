import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = app.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
        versionName = "1.0.0"
        versionCode = 1
    }

    buildTypes.forEach {
        it.buildConfigField(
            "String",
            "BEARER",
            "\"${
                gradleLocalProperties(project.rootDir).getProperty("github.bearer")
                    ?: throw IllegalArgumentException("No bearer provided")
            }\""
        )
    }

    dynamicFeatures.add(":user:presentation")
}
dependencies {
    implementation(project(":user:di"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core)
    implementation(presentation.bundles.compose)
}
