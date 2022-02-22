dependencyResolutionManagement {
    versionCatalogs {
        create("app") {
            version("compileSdk", "31")
            version("minSdk", "25")
        }

        create("libs") {
            version("kotlin", "1.6.10")

            alias("kotlin-gradle-plugin").to("org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
            alias("kotlin-stdlib").to("org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")


            alias("android-gradle-plugin").to("com.android.tools.build", "gradle").version("7.0.4")

            version("arrow", "1.0.1")
            alias("arrow-core").to("io.arrow-kt", "arrow-core").versionRef("arrow")


            version("compose-compiler", "1.1.0-rc03")
            version("compose", "1.1.0-rc03")
            version("activity", "1.3.1")
            alias("compose-ui").to("androidx.compose.ui", "ui").versionRef("compose")
            alias("compose-ui-tooling").to("androidx.compose.ui", "ui-tooling").versionRef("compose")
            alias("compose-foundation").to("androidx.compose.foundation", "foundation").versionRef("compose")
            alias("compose-material").to("androidx.compose.material", "material").versionRef("compose")
            alias("activity-compose").to("androidx.activity", "activity-compose").versionRef("activity")
            bundle(
                "compose",
                listOf(
                    "compose-ui",
                    "compose-ui-tooling",
                    "compose-foundation",
                    "compose-material",
                    "activity-compose",
                )
            )
        }
    }
}
