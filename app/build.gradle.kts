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

    dynamicFeatures.addAll(
        listOf(
            ":user",
            ":search",
        )
    )
}
dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:presentation"))
    implementation(project(":core:presentation:design"))

    implementation(project(":user:di"))
    implementation(project(":search:di"))
    implementation(project(":user:router"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core)
    implementation(presentation.bundles.compose)
    implementation(libs.arrow.core)
}
