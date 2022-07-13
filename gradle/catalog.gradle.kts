dependencyResolutionManagement {
    versionCatalogs {
        create("app") {
            version("compileSdk", "32")
            version("minSdk", "25")
        }

        create("libs") {
            version("kotlin", "1.6.10")

            alias("kotlin-gradle-plugin").to("org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
            alias("kotlin-stdlib").to("org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")

            version("kotlin-coroutine", "1.6.0")
            alias("kotlin-coroutine").to("org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlin-coroutine")

            alias("android-gradle-plugin").to("com.android.tools.build", "gradle").version("7.2.0")

            version("arrow", "1.0.1")
            alias("arrow-core").to("io.arrow-kt", "arrow-core").versionRef("arrow")

            version("dagger", "2.42")
            alias("dagger").to("com.google.dagger", "dagger").versionRef("dagger")
            alias("dagger-compiler").to("com.google.dagger", "dagger-compiler").versionRef("dagger")
            alias("inject").to("javax.inject", "javax.inject").version("1")

            version("androidx-core", "1.7.0")
            alias("androidx-core").to("androidx.core", "core").versionRef("androidx-core")
        }

        create("data") {
            version("apollo-graphql", "3.1.0")
            plugin("apollo-graphql-plugin", "com.apollographql.apollo3").versionRef("apollo-graphql")
            alias("apollo-graphql-runtime").to("com.apollographql.apollo3", "apollo-runtime").versionRef("apollo-graphql")
        }

        create("presentation") {
            version("compose-compiler", "1.1.0-rc03")

            version("compose", "1.1.0-rc03")
            alias("compose-ui").to("androidx.compose.ui", "ui").versionRef("compose")
            alias("compose-ui-tooling").to("androidx.compose.ui", "ui-tooling").versionRef("compose")
            alias("compose-foundation").to("androidx.compose.foundation", "foundation").versionRef("compose")

            version("material", "1.3.0-alpha01")
            alias("compose-material").to("androidx.compose.material", "material").versionRef("material")

            version("activity", "1.3.1")
            alias("activity-compose").to("androidx.activity", "activity-compose").versionRef("activity")

            version("coil", "2.0.0-rc01")
            alias("coil-compose").to("io.coil-kt", "coil-compose").versionRef("coil")

            version("accompanist", "0.24.4-alpha")
            alias("accompanist-placeholder").to("com.google.accompanist", "accompanist-placeholder-material").versionRef("accompanist")

            bundle(
                "compose",
                listOf(
                    "compose-ui",
                    "compose-ui-tooling",
                    "compose-foundation",
                    "compose-material",
                    "activity-compose",
                    "coil-compose",
                    "accompanist-placeholder"
                )
            )
        }

        create("test") {
            version("kotest", "5.3.2")
            alias("kotest").to("io.kotest", "kotest-runner-junit5").versionRef("kotest")

            version("mockk", "1.12.4")
            alias("mockk").to("io.mockk", "mockk").versionRef("mockk")

            version("truth", "1.1.3")
            alias("truth").to("com.google.truth", "truth").versionRef("truth")
            alias("truth-extensions").to("com.google.truth.extensions", "truth-java8-extension").versionRef("truth")

            version("turbine", "0.8.0")
            alias("turbine").to("app.cash.turbine", "turbine").versionRef("turbine")
        }
    }
}
