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
    implementation(project(":user:domain"))
    implementation(project(":user:data"))

    implementation(libs.apollo.graphql.runtime)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}
