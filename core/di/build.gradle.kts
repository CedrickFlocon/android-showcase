plugins {
    id("kotlin")
    id("kotlin-kapt")
}

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir("${buildDir.absolutePath}/generated/source/kapt/main/")
    }
}

dependencies {
    implementation(project(":core:data:graphql"))

    implementation(data.apollo.graphql.runtime)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}
