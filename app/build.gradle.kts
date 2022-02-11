plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = app.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
    }
}
dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(project(":user:presentation"))
}
