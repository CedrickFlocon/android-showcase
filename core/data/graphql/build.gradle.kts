plugins {
    id("kotlin")
    alias(data.plugins.apollo.graphql.plugin)
}

apollo {
    generateApolloMetadata.set(true)
    packageName.set("com.cedrickflocon.android.showcase.core.data.graphql")
    mapScalar("URI", "java.net.URI")
    alwaysGenerateTypesMatching.set(listOf(".*"))
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(data.apollo.graphql.runtime)
    implementation(libs.kotlin.coroutine)

    implementation(libs.inject)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
    testImplementation(test.truth.extensions)
}
