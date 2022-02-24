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
    ":user:data",
    ":user:domain",
    ":user:presentation",
)
