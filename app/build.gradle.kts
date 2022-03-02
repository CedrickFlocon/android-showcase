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

    dynamicFeatures.add(":user:presentation")
}
dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core)
}
