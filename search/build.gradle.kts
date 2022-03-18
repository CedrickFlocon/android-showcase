plugins {
    id("com.android.dynamic-feature")
}

android {
    compileSdk = app.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
    }
}

dependencies {
    implementation(project(":app"))

    implementation(project(":search:presentation"))
}
