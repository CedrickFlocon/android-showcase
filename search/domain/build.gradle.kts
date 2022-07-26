plugins {
    id("kotlin")
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.arrow.core)
    implementation(libs.kotlin.coroutine)

    implementation(libs.inject)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
    testImplementation(test.truth.extensions)
    testImplementation(test.turbine)
}
