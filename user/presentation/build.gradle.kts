plugins {
    id("com.android.library")
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
}

dependencies {
    implementation(project(":design"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutine)
    implementation(libs.bundles.compose)
}
