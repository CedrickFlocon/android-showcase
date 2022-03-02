plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
}

android {
    compileSdk = app.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":app"))

    implementation(project(":design"))
    implementation(project(":user:domain"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutine)
    implementation(libs.arrow.core)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.core)

    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.truth.extensions)
}
