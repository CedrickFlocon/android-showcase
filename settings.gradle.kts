rootProject.name = "Android Showcase"

enableFeaturePreview("VERSION_CATALOGS")
apply {
    from("gradle/catalog.gradle.kts")
}

include(
    ":app",
    ":design",
)

include(
    ":core:data:graphql",
    ":core:di",
)

include(
    ":user:di",
    ":user:data",
    ":user:domain",
    ":user:presentation",
)
