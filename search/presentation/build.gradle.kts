plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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

    resourcePrefix("showcase_search_")

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:presentation"))
    implementation(project(":core:presentation:design"))

    implementation(project(":search:domain"))
    implementation(project(":search:di"))

    implementation(project(":user:router"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutine)
    implementation(presentation.bundles.compose)
    implementation(libs.androidx.core)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.inject)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
    testImplementation(test.truth.extensions)
    testImplementation(test.turbine)
}
