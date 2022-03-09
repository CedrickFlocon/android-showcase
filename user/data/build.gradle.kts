plugins {
    id("kotlin")
    alias(data.plugins.apollo.graphql.plugin)
}

apollo {
    packageName.set("com.cedrickflocon.android.showcase.user.data")
    mapScalar("URI", "java.net.URI")
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(project(":user:domain"))

    implementation(libs.kotlin.stdlib)
    implementation(data.apollo.graphql.runtime)
    implementation(libs.arrow.core)
    implementation(libs.kotlin.coroutine)

    implementation(libs.inject)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
    testImplementation(test.truth.extensions)
}
