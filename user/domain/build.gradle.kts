plugins {
    id("kotlin")
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.arrow.core)
    implementation(libs.kotlin.coroutine)

    implementation(libs.inject)

    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.truth.extensions)
}
