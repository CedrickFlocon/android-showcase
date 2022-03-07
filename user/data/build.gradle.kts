plugins {
    id("kotlin")
    alias(libs.plugins.apollo.graphql.plugin)
}

apollo {
    packageName.set("com.cedrickflocon.android.showcase.user.data")
    mapScalar("URI", "java.net.URI")
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(project(":user:domain"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.apollo.graphql.runtime)
    implementation(libs.arrow.core)
    implementation(libs.kotlin.coroutine)

    implementation(libs.inject)

    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.truth.extensions)
}
