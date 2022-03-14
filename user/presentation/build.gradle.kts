plugins {
    id("com.android.dynamic-feature")
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

    resourcePrefix("showcase_user_")

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

kapt {
    arguments {
        arg("dagger.validateTransitiveComponentDependencies", "DISABLED")
    }
}

dependencies {
    implementation(project(":app"))

    implementation(project(":design"))
    implementation(project(":user:domain"))
    implementation(project(":user:di"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutine)
    implementation(libs.arrow.core)
    implementation(presentation.bundles.compose)
    implementation(libs.androidx.core)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.inject)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
    testImplementation(test.truth.extensions)
}
