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

            version("kotlin-coroutine", "1.6.0")
            alias("kotlin-coroutine").to("org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlin-coroutine")

            alias("android-gradle-plugin").to("com.android.tools.build", "gradle").version("7.0.4")

            version("arrow", "1.0.1")
            alias("arrow-core").to("io.arrow-kt", "arrow-core").versionRef("arrow")


            version("compose-compiler", "1.1.0-rc03")
            version("compose", "1.1.0-rc03")
            version("material3", "1.0.0-alpha05")
            version("activity", "1.3.1")
            alias("compose-ui").to("androidx.compose.ui", "ui").versionRef("compose")
            alias("compose-ui-tooling").to("androidx.compose.ui", "ui-tooling").versionRef("compose")
            alias("compose-foundation").to("androidx.compose.foundation", "foundation").versionRef("compose")
            alias("compose-material3").to("androidx.compose.material3", "material3").versionRef("material3")
            alias("activity-compose").to("androidx.activity", "activity-compose").versionRef("activity")
            bundle(
                "compose",
                listOf(
                    "compose-ui",
                    "compose-ui-tooling",
                    "compose-foundation",
                    "compose-material3",
                    "activity-compose",
                )
            )

            version("kotest", "5.1.0")
            alias("kotest").to("io.kotest", "kotest-runner-junit5").versionRef("kotest")

            version("mockk", "1.12.2")
            alias("mockk").to("io.mockk", "mockk").versionRef("mockk")

            version("truth", "1.1.3")
            alias("truth").to("com.google.truth", "truth").versionRef("truth")
            alias("truth-extensions").to("com.google.truth.extensions", "truth-java8-extension").versionRef("truth")
        }
    }
}
