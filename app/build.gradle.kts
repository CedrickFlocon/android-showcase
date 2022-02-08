plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 25
    }
}
dependencies {
    implementation(libs.kotlin.stdlib)
}
