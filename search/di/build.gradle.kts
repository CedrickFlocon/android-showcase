plugins {
    id("kotlin")
    id("kotlin-kapt")
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
