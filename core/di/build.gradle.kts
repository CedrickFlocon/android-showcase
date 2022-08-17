plugins {
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":core:data:graphql"))

    implementation(data.apollo.graphql.runtime)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}
