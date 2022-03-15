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
    implementation(project(":core:di"))


    implementation(project(":search:domain"))
    implementation(project(":search:data"))

    implementation(data.apollo.graphql.runtime)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}
