plugins {
    id("kotlin")
    alias(data.plugins.apollo.graphql.plugin)
}

apollo {
    packageName.set("com.cedrickflocon.android.showcase.user.data")
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(project(":search:domain"))

    implementation(libs.kotlin.stdlib)
    implementation(data.apollo.graphql.runtime)
    apolloMetadata(project(":core:data:graphql"))
    implementation(project(":core:data:graphql"))
    implementation(libs.arrow.core)
    implementation(libs.kotlin.coroutine)

    implementation(libs.inject)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
    testImplementation(test.truth.extensions)
}
