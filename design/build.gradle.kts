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
        kotlinCompilerExtensionVersion = presentation.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(presentation.bundles.compose)
}
